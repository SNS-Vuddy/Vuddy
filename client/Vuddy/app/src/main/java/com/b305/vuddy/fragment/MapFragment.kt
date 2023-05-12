package com.b305.vuddy.fragment

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
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
import com.b305.vuddy.util.BASE_PROFILE_IMG_URL
import com.b305.vuddy.util.BASIC_IMG_URL
import com.b305.vuddy.util.GOTOHOME_IMG_URL
import com.b305.vuddy.util.GOTOWORK_IMG_URL
import com.b305.vuddy.util.HOME_IMG_URL
import com.b305.vuddy.util.LocationProvider
import com.b305.vuddy.util.OFFICE_IMG_URL
import com.b305.vuddy.util.SLEEP_IMG_URL
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

    private suspend fun getProfileBitmap(imgUrl: String): Bitmap = withContext(Dispatchers.IO) {
        return@withContext Glide.with(requireContext())
            .asBitmap()
            .load(imgUrl)
            .circleCrop()
            .override(150, 150)
            .submit()
            .get()
    }

    private suspend fun getStatusBitmap(imgUrl: String): Bitmap = withContext(Dispatchers.IO) {
        var statusUrl: String = BASIC_IMG_URL
        when (imgUrl) {
            "home" -> statusUrl = HOME_IMG_URL
            "office" -> statusUrl = OFFICE_IMG_URL
            "gotowork" -> statusUrl = GOTOWORK_IMG_URL
            "gotohome" -> statusUrl = GOTOHOME_IMG_URL
            "sleep" -> statusUrl = SLEEP_IMG_URL
        }

        return@withContext Glide.with(requireContext())
            .asBitmap()
            .load(statusUrl)
            .override(220, 220)
            .submit()
            .get()
    }

    private suspend fun getMarkerOptions(location: LatLng, imgUrl: String, status: String): MarkerOptions =
        withContext(Dispatchers.IO) {
            val markerOptions = MarkerOptions()
            val profileBitmap = getProfileBitmap(imgUrl)
            val statusBitmap = getStatusBitmap(status)

            // 결과 비트맵 크기를 statusBitmap의 크기로 설정
            val resultBitmap = Bitmap.createBitmap(statusBitmap.width, statusBitmap.height, statusBitmap.config)
            val canvas = Canvas(resultBitmap)

            // 상태 이미지를 캔버스에 그림
            canvas.drawBitmap(statusBitmap, 0f, 0f, null)

            // 프로필 이미지의 중심 좌표 계산
            val profileTop = (resultBitmap.height - profileBitmap.height + 30f) / 2f
            val profileLeft = (resultBitmap.width - profileBitmap.width) / 2f
            canvas.drawBitmap(profileBitmap, profileLeft, profileTop, null)

            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resultBitmap)
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16f))

            if (marker == null) {
                val currentUser = sharedManager.getCurrentUser()
                var profileImgUrl: String = currentUser.imgUrl ?: BASE_PROFILE_IMG_URL
                if (profileImgUrl.isEmpty()) {
                    profileImgUrl = BASE_PROFILE_IMG_URL
                    currentUser.imgUrl = BASE_PROFILE_IMG_URL
                    sharedManager.saveCurrentUser(currentUser)
                }

                val markerOption = getMarkerOptions(location, profileImgUrl, "home")
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
                    val imgUrl = userLocation.imgUrl ?: BASE_PROFILE_IMG_URL
                    val newLocation = LatLng(latitude, longitude)
                    val existingMarker = markerMap[nickname]

                    if (existingMarker == null) {
                        val markerOption = getMarkerOptions(newLocation, imgUrl, "home")
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
            val location = LatLng(latitude, longitude)

            if (marker == null) {
                val currentUser = sharedManager.getCurrentUser()
                var profileImgUrl: String = currentUser.imgUrl ?: BASE_PROFILE_IMG_URL
                if (profileImgUrl.isEmpty()) {
                    profileImgUrl = BASE_PROFILE_IMG_URL
                    currentUser.imgUrl = BASE_PROFILE_IMG_URL
                    sharedManager.saveCurrentUser(currentUser)
                }
                val markerOption = getMarkerOptions(location, profileImgUrl, "home")
                marker = mMap.addMarker(markerOption)
            } else {
                animateMarkerTo(marker!!, location)
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
                val newProfile = userLocation.imgUrl!!
                val newStatus = userLocation.status!!
                val newLocation = LatLng(latitude, longitude)
                val existingMarker = markerMap[nickname]

                if (existingMarker == null) {
                    val markerOption = getMarkerOptions(newLocation, newProfile, newStatus)
                    val newMarker = mMap.addMarker(markerOption)
                    markerMap[nickname] = newMarker!!
                } else {
                    val markerOptions = getMarkerOptions(newLocation, newProfile, newStatus)
                    existingMarker.setIcon(markerOptions.icon)
                    animateMarkerTo(existingMarker, newLocation)
                }
            }
        }
    }
}
