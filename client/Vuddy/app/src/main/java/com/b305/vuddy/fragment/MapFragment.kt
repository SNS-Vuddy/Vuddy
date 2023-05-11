package com.b305.vuddy.fragment

import android.animation.ValueAnimator
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
import com.b305.vuddy.util.LocationProvider
import com.b305.vuddy.util.SharedManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
//            requireActivity().stopService(Intent(requireContext(), ImmortalLocationService::class.java))
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
        refreshMarker(true)
    }

    private fun refreshMarkerWithOutLocationProvider(userLocation: UserLocation) {
        if (!::mMap.isInitialized) {
            return
        }
        requireActivity().runOnUiThread {
            val latitude = userLocation.lat!!.toDouble()
            val longitude = userLocation.lng!!.toDouble()
            val newLocation = LatLng(latitude, longitude)

            if (marker == null) {
                val markerOption = MarkerOptions().position(newLocation)
                marker = mMap.addMarker(markerOption)
            } else {
                animateMarkerTo(marker!!, newLocation)
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
                        val markerOption = MarkerOptions().position(newLocation).title(nickname)
                        val newMarker = mMap.addMarker(markerOption)!!
                        markerMap[nickname] = newMarker
                    } else {
                        animateMarkerTo(existingMarker, newLocation)
                    }
                }
            }
        }
    }

    private fun refreshMarker(isMoveCamera: Boolean) {
        if (!::mMap.isInitialized) {
            return
        }
        requireActivity().runOnUiThread {
            locationProvider = LocationProvider(requireContext())
            var latitude = locationProvider!!.getLocationLatitude()!!
            var longitude = locationProvider!!.getLocationLongitude()!!
            var location = LatLng(latitude, longitude)

            mMap.clear()

            if (isMoveCamera) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
            }

            if (marker == null) {
                val markerOption = MarkerOptions().position(location)
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
                        val markerOption = MarkerOptions().position(newLocation).title(nickname)
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

    var count = 0

    @Subscribe
    fun onLocationEvent(locationEvent: LocationEvent) {
        if (locationEvent.isMe) {
            // temp
            binding.tvTime.text = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault()))
            binding.tvLatitude.text = locationEvent.userLocation.lat
            binding.tvLongitude.text = locationEvent.userLocation.lng
            count++
            binding.tvCount.text = count.toString()
            refreshMarkerWithOutLocationProvider(locationEvent.userLocation)
            return
        }
        val friendLocation = locationEvent.userLocation
        sharedManager.addUserLocationList(friendLocation)
        refreshMarkerWithOutLocationProvider(friendLocation)
    }
}
