package com.b305.vuddy.fragment

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentMapBinding
import com.b305.vuddy.model.LocationEvent
import com.b305.vuddy.model.UserLocation
import com.b305.vuddy.service.ImmortalService
import com.b305.vuddy.util.LocationProvider
import com.b305.vuddy.util.SharedManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentMapBinding
    private val sharedManager: SharedManager by lazy { SharedManager(requireContext()) }
    var locationProvider: LocationProvider? = null
    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null
    private val markerMap = mutableMapOf<String, Marker>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)

        binding.ivFriend.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_friendFragment)
        }

        binding.ivWrite.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_writeFeedFragment)
        }

        binding.ivMessage.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_messageFragment)
        }

        binding.ivProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_profileFragment)
        }

        binding.fabLogout.setOnClickListener {
            sharedManager.removeCurrentToken()
            sharedManager.removeCurrentUser()
            sharedManager.removeUserLocationList()
            requireActivity().stopService(Intent(requireContext(), ImmortalService::class.java))
            it.findNavController().navigate(R.id.action_mapFragment_to_signupActivity)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        refreshMarker()
    }

    private suspend fun getBitmapFromURL(imgUrl: String): Bitmap = withContext(Dispatchers.IO) {
        return@withContext Glide.with(requireContext())
            .asBitmap()
            .load(imgUrl)
            .circleCrop()
            .override(150, 150)
            .submit()
            .get()
    }

    private suspend fun getMarkerOptions(location: LatLng, imgUrl: String): MarkerOptions =
        withContext(Dispatchers.IO) {
            val markerOptions = MarkerOptions()
            val bitmap = getBitmapFromURL(imgUrl)
            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
            markerOptions.icon(bitmapDescriptor)
                .position(location)
            return@withContext markerOptions
        }


    private fun refreshMarker() {
        if (!::mMap.isInitialized) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            locationProvider = LocationProvider(requireContext())
            var latitude = locationProvider!!.getLocationLatitude()!!
            var longitude = locationProvider!!.getLocationLongitude()!!
            var location = LatLng(latitude, longitude)

            mMap.clear()
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))

            if (marker == null) {
//                val imgUrl = sharedManager.getCurrentUser().imgUrl
                val imgUrl =
                    "https://vuddy-s3-bucket1.s3.amazonaws.com/images/7646204d-81a4-4cc2-97ff-688e84bd6793_gastonabascal8F6pXyQyLUunsplash.jpg"
                val markerOption = getMarkerOptions(location, imgUrl)
                marker = mMap.addMarker(markerOption)
            } else {
                animateMarkerTo(marker!!, location)
            }

            val userLocationList = sharedManager.getUserLocationList()
            for (userLocation in userLocationList) {
                val nickname = userLocation.nickname
                if (nickname != null) {
                    val latitude = userLocation.lat!!.toDouble()
                    val longitude = userLocation.lng!!.toDouble()
                    val newLocation = LatLng(latitude, longitude)
                    val existingMarker = markerMap[nickname]

                    if (existingMarker == null) {
                        val imgUrl =
                            "https://vuddy-s3-bucket1.s3.amazonaws.com/images/508a80f8-0b9b-490a-928b-31aeabb9c1bb_reccccc.png"
                        val markerOption = getMarkerOptions(newLocation, imgUrl)
                        val newMarker = mMap.addMarker(markerOption)
                        markerMap[nickname] = newMarker!!
                    } else {
                        animateMarkerTo(existingMarker, newLocation)
                    }
                }
            }
        }
    }

    private fun animateMarkerTo(marker: Marker, targetPosition: LatLng) {
        val startPosition = marker.position
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 300
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction
            val lat = startPosition.latitude + (targetPosition.latitude - startPosition.latitude) * fraction
            val lng = startPosition.longitude + (targetPosition.longitude - startPosition.longitude) * fraction
            marker.position = LatLng(lat, lng)
        }
        valueAnimator.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onLocationEvent(event: LocationEvent) {
        if (event.isMyLocation) {
            val userLocation = event.userLocation
            updateMyMarker(userLocation)
        } else {
            val friendLocation = event.userLocation
            sharedManager.addUserLocationList(friendLocation)
            updateFriendMarkers(friendLocation)
        }
    }

    private fun updateMyMarker(userLocation: UserLocation) {
        if (!::mMap.isInitialized) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            val latitude = userLocation.lat!!.toDouble()
            val longitude = userLocation.lng!!.toDouble()
            val newLocation = LatLng(latitude, longitude)

            if (marker == null) {
//                val imgUrl = sharedManager.getCurrentUser().imgUrl
                val imgUrl =
                    "https://vuddy-s3-bucket1.s3.amazonaws.com/images/7646204d-81a4-4cc2-97ff-688e84bd6793_gastonabascal8F6pXyQyLUunsplash.jpg"
                val markerOption = getMarkerOptions(newLocation, imgUrl)
                marker = mMap.addMarker(markerOption)
            } else {
                animateMarkerTo(marker!!, newLocation)
            }
        }
    }

    private fun updateFriendMarkers(userLocation: UserLocation) {
        if (!::mMap.isInitialized) {
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            val nickname = userLocation.nickname
            if (nickname != null) {
                val latitude = userLocation.lat!!.toDouble()
                val longitude = userLocation.lng!!.toDouble()
                val newLocation = LatLng(latitude, longitude)
                val existingMarker = markerMap[nickname]

                if (existingMarker == null) {
//                    val imgUrl=userLocation.imgUrl
                    val imgUrl =
                        "https://vuddy-s3-bucket1.s3.amazonaws.com/images/508a80f8-0b9b-490a-928b-31aeabb9c1bb_reccccc.png"
                    val markerOption = getMarkerOptions(newLocation, imgUrl)

                    val newMarker = mMap.addMarker(markerOption)
                    markerMap[nickname] = newMarker!!
                } else {
                    animateMarkerTo(existingMarker, newLocation)
                }
            }
        }
    }
}
