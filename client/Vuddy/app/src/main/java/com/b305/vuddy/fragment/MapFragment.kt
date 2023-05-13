package com.b305.vuddy.fragment

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentMapBinding
import com.b305.vuddy.model.UserLocation
import com.b305.vuddy.service.ImmortalService
import com.b305.vuddy.util.BASE_PROFILE_IMG_URL
import com.b305.vuddy.util.BASIC_IMG_URL
import com.b305.vuddy.util.FEED_MODE
import com.b305.vuddy.util.FRIEND_FEED_MODE
import com.b305.vuddy.util.GOTOHOME_IMG_URL
import com.b305.vuddy.util.GOTOWORK_IMG_URL
import com.b305.vuddy.util.HOME_IMG_URL
import com.b305.vuddy.util.LocationProvider
import com.b305.vuddy.util.MAP_MODE
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentMapBinding
    private val sharedManager: SharedManager by lazy { SharedManager(requireContext()) }
    private lateinit var locationProvider: LocationProvider
    private lateinit var mMap: GoogleMap
    private lateinit var markerOptionsMap: MutableMap<String, MarkerOptions>
    private lateinit var markersMap: MutableMap<String, Marker>
    private var markerMode: Int = MAP_MODE

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (!::locationProvider.isInitialized) {
            locationProvider = LocationProvider(requireContext())
        }
        if (!::markersMap.isInitialized) {
            markersMap = mutableMapOf<String, Marker>()
        }
        if (!::markerOptionsMap.isInitialized) {
            markerOptionsMap = mutableMapOf<String, MarkerOptions>()
        }

        val nickname = sharedManager.getCurrentUser().nickname!!
        val latitude = locationProvider.getLocationLatitude()!!
        val longitude = locationProvider.getLocationLongitude()!!
        val latLng = LatLng(latitude, longitude)
        //Todo 이거 API로 바꿔야함
        val profileImgUrl = BASE_PROFILE_IMG_URL
        val statusImgUrl = BASIC_IMG_URL
        val marKerOptions = makeMarkerOptions(latLng, profileImgUrl, statusImgUrl)
        markerOptionsMap[nickname] = marKerOptions

        refreshMap(true)
    }

    private fun refreshMap(isMoveCamera: Boolean) {
        if (!::mMap.isInitialized) {
            return
        }


        //Todo 모드 테스트
        if (markerMode == MAP_MODE) {
            markerOptionsMap.forEach { (nickname, markerOptions) ->
                val marker = markersMap[nickname]
                if (marker != null) {
                    animateMarkerTo(marker, markerOptions.position)
                } else {
                    val newMarker = mMap.addMarker(markerOptions)!!
                    markersMap[nickname] = newMarker
                }
            }

            if (isMoveCamera) {
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        markersMap[sharedManager.getCurrentUser().nickname]!!.position,
                        15f
                    )
                )
            }
            return
        }

        // Map mode 아닌 경우 내 위치 마커 제외 하고 전부 삭제
        val currentNickname = sharedManager.getCurrentUser().nickname!!
        markersMap.filterKeys { it != currentNickname }
            .forEach { (nickname, marker) ->
                marker.remove()
                markersMap.remove(nickname)
            }
        markerOptionsMap.filterKeys { it != currentNickname }
            .forEach { (nickname, _) ->
                markerOptionsMap.remove(nickname)
            }

        if (markerMode == FEED_MODE) {
            val markerOptions = markerOptionsMap[currentNickname]!!
            val marker = markersMap[currentNickname]

            if (marker != null) {
                animateMarkerTo(marker, markerOptions.position)
            } else {
                val newMarker = mMap.addMarker(markerOptions)!!
                markersMap[currentNickname] = newMarker
            }
            if (isMoveCamera) {
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        markersMap[sharedManager.getCurrentUser().nickname]!!.position,
                        15f
                    )
                )
            }
            return
        }
        if (markerMode == FRIEND_FEED_MODE) {
            val markerOptions = markerOptionsMap[currentNickname]!!
            val marker = markersMap[currentNickname]

            if (marker != null) {
                animateMarkerTo(marker, markerOptions.position)
            } else {
                val newMarker = mMap.addMarker(markerOptions)!!
                markersMap[currentNickname] = newMarker
            }
            if (isMoveCamera) {
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        markersMap[sharedManager.getCurrentUser().nickname]!!.position,
                        15f
                    )
                )
            }
            return
        }
    }

    private fun animateMarkerTo(marker: Marker, targetPosition: LatLng) {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 1000
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction
            val lat = marker.position.latitude + (targetPosition.latitude - marker.position.latitude) * fraction
            val lng = marker.position.longitude + (targetPosition.longitude - marker.position.longitude) * fraction
            marker.position = LatLng(lat, lng)
        }
        valueAnimator.start()
    }

    private fun makeMarkerOptions(latLng: LatLng, profileImgUrl: String, statusImgUrl: String): MarkerOptions =
        runBlocking {
            val profileBitmap = makeProfileBitmap(profileImgUrl)
            val statusBitmap = makeStatusBitmap(statusImgUrl)

            val resultBitmap = Bitmap.createBitmap(statusBitmap.width, statusBitmap.height, statusBitmap.config)
            val canvas = Canvas(resultBitmap)
            canvas.drawBitmap(statusBitmap, 0f, 0f, null)
            val profileTop = (resultBitmap.height - profileBitmap.height + 30f) / 2f
            val profileLeft = (resultBitmap.width - profileBitmap.width) / 2f
            canvas.drawBitmap(profileBitmap, profileLeft, profileTop, null)
            val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resultBitmap)

            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
                .icon(bitmapDescriptor)
            return@runBlocking markerOptions
        }

    private suspend fun makeProfileBitmap(imgUrl: String): Bitmap = withContext(Dispatchers.IO) {
        return@withContext Glide.with(requireContext())
            .asBitmap()
            .load(imgUrl)
            .circleCrop()
            .override(150, 150)
            .submit()
            .get()
    }

    private suspend fun makeStatusBitmap(imgUrl: String): Bitmap = withContext(Dispatchers.IO) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onLocationEvent(userLocation: UserLocation) {
        if (!::locationProvider.isInitialized) {
            locationProvider = LocationProvider(requireContext())
        }
        if (!::markerOptionsMap.isInitialized) {
            markerOptionsMap = mutableMapOf<String, MarkerOptions>()
        }

        val nickname = userLocation.nickname!!
        val latitude = userLocation.latitude?.toDouble()!!
        val longitude = userLocation.longitude?.toDouble()!!
        val latLng = LatLng(latitude, longitude)
        //Todo 이거 API로 바꿔야함
        val profileImgUrl = BASE_PROFILE_IMG_URL
        val statusImgUrl = BASIC_IMG_URL
        val marKerOptions = makeMarkerOptions(latLng, profileImgUrl, statusImgUrl)
        markerOptionsMap[nickname] = marKerOptions

        refreshMap(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)

        val fabSelect = ContextCompat.getColorStateList(requireContext(), R.color.selected)
        val fabUnselect = ContextCompat.getColorStateList(requireContext(), R.color.unselected)

        binding.fabMapMode.setOnClickListener {
            Log.d("MapFragment", "****MapMode")
            markerMode = MAP_MODE
            binding.fabMapMode.backgroundTintList = fabSelect
            binding.fabFeedMode.backgroundTintList = fabUnselect
            binding.fabFriendFeedMode.backgroundTintList = fabUnselect
        }
        binding.fabFeedMode.setOnClickListener {
            Log.d("MapFragment", "****FeedMode")
            markerMode = FEED_MODE
            binding.fabMapMode.backgroundTintList = fabUnselect
            binding.fabFeedMode.backgroundTintList = fabSelect
            binding.fabFriendFeedMode.backgroundTintList = fabUnselect
        }
        binding.fabFriendFeedMode.setOnClickListener {
            Log.d("MapFragment", "****FriendFeedMode")
            markerMode = FRIEND_FEED_MODE
            binding.fabMapMode.backgroundTintList = fabUnselect
            binding.fabFeedMode.backgroundTintList = fabUnselect
            binding.fabFriendFeedMode.backgroundTintList = fabSelect
        }

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
}
