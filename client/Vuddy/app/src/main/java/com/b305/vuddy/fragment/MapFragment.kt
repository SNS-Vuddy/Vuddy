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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentMapBinding
import com.b305.vuddy.model.MapFeedResponse
import com.b305.vuddy.model.User
import com.b305.vuddy.model.UserLocation
import com.b305.vuddy.service.ImmortalService
import com.b305.vuddy.util.ANIMATE_CAMERA
import com.b305.vuddy.util.BASIC_IMG_URL
import com.b305.vuddy.util.FEED_MODE
import com.b305.vuddy.util.FRIEND_FEED_MODE
import com.b305.vuddy.util.GOTOHOME_IMG_URL
import com.b305.vuddy.util.GOTOWORK_IMG_URL
import com.b305.vuddy.util.HOME_IMG_URL
import com.b305.vuddy.util.LocationProvider
import com.b305.vuddy.util.MAP_MODE
import com.b305.vuddy.util.MOVE_CAMERA
import com.b305.vuddy.util.NOT_MOVE_CAMERA
import com.b305.vuddy.util.OFFICE_IMG_URL
import com.b305.vuddy.util.RetrofitAPI
import com.b305.vuddy.util.SLEEP_IMG_URL
import com.b305.vuddy.util.SharedManager
import com.b305.vuddy.util.ZOOM_LEVEL
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
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentMapBinding
    private val sharedManager: SharedManager by lazy { SharedManager(requireContext()) }
    private lateinit var locationProvider: LocationProvider
    private lateinit var mMap: GoogleMap
    private lateinit var markerOptionsMap: MutableMap<String, MarkerOptions>
    private lateinit var markersMap: MutableMap<String, Marker>
    private lateinit var markerBitmapMap: MutableMap<String, Bitmap>
    private var markerMode: Int = MAP_MODE
    private lateinit var currentUser: User
    private lateinit var currentNickname: String
    private lateinit var currentProfileImgUrl: String
    private lateinit var currentStatusImgUrl: String

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("MapFragment", "****onMapReady")
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
        if (!::markerBitmapMap.isInitialized) {
            markerBitmapMap = mutableMapOf<String, Bitmap>()
        }
        currentUser = sharedManager.getCurrentUser()
        currentNickname = currentUser.nickname!!
        currentProfileImgUrl = currentUser.profileImgUrl!!
        currentStatusImgUrl = currentUser.statusImgUrl!!

        val latitude = locationProvider.getLocationLatitude()!!
        val longitude = locationProvider.getLocationLongitude()!!
        val latLng = LatLng(latitude, longitude)
        val marKerOptions = makeMarkerOptions(currentNickname, latLng, currentProfileImgUrl, currentStatusImgUrl)

        markerOptionsMap[currentNickname] = marKerOptions
        refreshMap(MOVE_CAMERA)

        mMap.setOnMarkerClickListener {
            val clickedMarkerNickname = markersMap.entries.find { entry ->
                entry.value == it
            }?.key

            //Todo 친구 위치 마커 클릭시 : 바텀 시트
            if (clickedMarkerNickname != null && clickedMarkerNickname != currentNickname && !clickedMarkerNickname.startsWith("FEED:")) {
                val clickedMarkerIcon = markerOptionsMap[clickedMarkerNickname]?.icon
                if (clickedMarkerIcon != null) {
                    val clickedMarkerBitmap = markerBitmapMap[clickedMarkerNickname]!!
                    val friendMarkerBottomSheetFragment = FriendMarkerBottomSheetFragment.newInstance(clickedMarkerNickname, clickedMarkerBitmap)
                    friendMarkerBottomSheetFragment.show(requireActivity().supportFragmentManager, friendMarkerBottomSheetFragment.tag)
                }
            }

            //Todo 피드 마커 클릭시 : 피드 바텀 시트
            if (clickedMarkerNickname != null && clickedMarkerNickname != currentNickname && clickedMarkerNickname.startsWith("FEED:")) {
                val feedId = clickedMarkerNickname.split(":")[1].toInt()
                val feedMarkerBottomSheetFragment = FeedMarkerBottomSheetFragment.newInstance(feedId)
                feedMarkerBottomSheetFragment.show(requireActivity().supportFragmentManager, feedMarkerBottomSheetFragment.tag)
            }

            if (clickedMarkerNickname == currentNickname) {
                val latLng = it.position
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL)
                mMap.animateCamera(cameraUpdate)
            }
            true
        }
    }

    private fun refreshMap(cameraMode: Int) {
        if (!::mMap.isInitialized) {
            return
        }

        markerOptionsMap.forEach { (nickname, markerOptions) ->
            val marker = markersMap[nickname]
            if (marker != null) {
                animateMarkerTo(marker, markerOptions.position)
            } else {
                val newMarker = mMap.addMarker(markerOptions)!!
                markersMap[nickname] = newMarker
            }
        }

        val latLng = markersMap[sharedManager.getCurrentUser().nickname]!!.position
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL)
        when (cameraMode) {
            MOVE_CAMERA -> {
                mMap.moveCamera(cameraUpdate)
            }

            ANIMATE_CAMERA -> {
                mMap.animateCamera(cameraUpdate)
            }

            NOT_MOVE_CAMERA -> {
                // do nothing
            }
        }
        return
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

    private fun makeMarkerOptions(nickname: String, latLng: LatLng, profileImgUrl: String, statusImgUrl: String): MarkerOptions =
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
            //Todo bitmap
            markerBitmapMap[nickname] = resultBitmap
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

        if (!::markersMap.isInitialized) {
            markersMap = mutableMapOf<String, Marker>()
        }
        if (!::markerOptionsMap.isInitialized) {
            markerOptionsMap = mutableMapOf<String, MarkerOptions>()
        }
        if (!::markerBitmapMap.isInitialized) {
            markerBitmapMap = mutableMapOf<String, Bitmap>()
        }

        if (markerMode != MAP_MODE && userLocation.nickname != currentNickname) {
            return
        }

        val nickname = userLocation.nickname!!
        val latitude = userLocation.latitude?.toDouble()!!
        val longitude = userLocation.longitude?.toDouble()!!
        val latLng = LatLng(latitude, longitude)
        val profileImgUrl = userLocation.profileImgUrl!!
        val statusImgUrl = userLocation.statusImgUrl!!
        val marKerOptions = makeMarkerOptions(nickname, latLng, profileImgUrl, statusImgUrl)
        markerOptionsMap[nickname] = marKerOptions
        refreshMap(NOT_MOVE_CAMERA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)

        val fabSelect = ContextCompat.getColorStateList(requireContext(), R.color.selected)
        val fabUnselect = ContextCompat.getColorStateList(requireContext(), R.color.unselected)

        binding.fabMapMode.setOnClickListener {
            if (markerMode == MAP_MODE) {
                return@setOnClickListener
            }
            Log.d("MapFragment", "****MapMode")
            markerMode = MAP_MODE
            binding.fabMapMode.backgroundTintList = fabSelect
            binding.fabFeedMode.backgroundTintList = fabUnselect
            binding.fabFriendFeedMode.backgroundTintList = fabUnselect

            markersMap.filterKeys { it != currentNickname }.forEach { (nickname, marker) ->
                marker.remove()
                markersMap.remove(nickname)
            }
            markerOptionsMap.filterKeys { it != currentNickname }.forEach { (nickname, _) ->
                markerOptionsMap.remove(nickname)
            }

            refreshMap(ANIMATE_CAMERA)
        }

        binding.fabFeedMode.setOnClickListener {
            if (markerMode == FEED_MODE) {
                return@setOnClickListener
            }
            Log.d("MapFragment", "****FeedMode")
            markerMode = FEED_MODE
            binding.fabMapMode.backgroundTintList = fabUnselect
            binding.fabFeedMode.backgroundTintList = fabSelect
            binding.fabFriendFeedMode.backgroundTintList = fabUnselect

            val call = RetrofitAPI.mapFeedService
            call.getAllFriendsFeed().enqueue(object : Callback<MapFeedResponse> {
                override fun onResponse(call: Call<MapFeedResponse>, response: Response<MapFeedResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()?.mapFeedList
                        result?.forEach { mapFeed ->
                            val feedId = "FEED:${mapFeed.feedId}"
                            val imgUrl = mapFeed.imgUrl
                            val location = mapFeed.location
                            val (latitudeStr, longitudeStr) = location.split(",")
                            val latitude: Double = latitudeStr.trim().toDouble()
                            val longitude: Double = longitudeStr.trim().toDouble()
                            val latLng = LatLng(latitude, longitude)
                            Log.d("MapFragment", "****FeedMode result : $feedId, $imgUrl, $latLng")

                            if (!::locationProvider.isInitialized) {
                                locationProvider = LocationProvider(requireContext())
                            }
                            if (!::markersMap.isInitialized) {
                                markersMap = mutableMapOf<String, Marker>()
                            }
                            if (!::markerOptionsMap.isInitialized) {
                                markerOptionsMap = mutableMapOf<String, MarkerOptions>()
                            }
                            if (!::markerBitmapMap.isInitialized) {
                                markerBitmapMap = mutableMapOf<String, Bitmap>()
                            }

                            val statusImgUrl = BASIC_IMG_URL
                            val marKerOptions = makeMarkerOptions(feedId, latLng, imgUrl, statusImgUrl)
                            markerOptionsMap[feedId] = marKerOptions

                            val existingMarker = markersMap[feedId]
                            if (existingMarker != null) {
                                animateMarkerTo(existingMarker, marKerOptions.position)
                            } else {
                                val newMarker = mMap.addMarker(marKerOptions)!!
                                markersMap[feedId] = newMarker
                            }
                        }

                        if (result != null) {
                            markersMap.filterKeys { it != currentNickname && !result.any { feed -> "FEED:${feed.feedId}" == it } }
                                .forEach { (nickname, marker) ->
                                    marker.remove()
                                    markersMap.remove(nickname)
                                }
                        }
                        if (result != null) {
                            markerOptionsMap.filterKeys { it != currentNickname && !result.any { feed -> "FEED:${feed.feedId}" == it } }
                                .forEach { (nickname, _) ->
                                    markerOptionsMap.remove(nickname)
                                }
                        }

                        refreshMap(ANIMATE_CAMERA)
                    } else {
                        val errorMessage = JSONObject(response.errorBody()?.string()!!)
                        Log.d("MapFragment", "****FeedMode errorMessage : $errorMessage")
                        Toast.makeText(context, errorMessage.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MapFeedResponse>, t: Throwable) {
                    Log.d("MapFragment", "****FeedMode onFailure : ${t.message}")
                    Toast.makeText(context, "피드 불러오기 실패", Toast.LENGTH_SHORT).show()
                }
            })
        }
        binding.fabFriendFeedMode.setOnClickListener {
            if (markerMode == FRIEND_FEED_MODE) {
                return@setOnClickListener
            }
            Log.d("MapFragment", "****FriendFeedMode")
            markerMode = FRIEND_FEED_MODE
            binding.fabMapMode.backgroundTintList = fabUnselect
            binding.fabFeedMode.backgroundTintList = fabUnselect
            binding.fabFriendFeedMode.backgroundTintList = fabSelect

            //Todo API 테스트 인자 지금 admin1인데 나중에 바꿔야 함
            val call = RetrofitAPI.mapFeedService
            call.getOneFriendFeed("admin1").enqueue(object : Callback<MapFeedResponse> {
                override fun onResponse(call: Call<MapFeedResponse>, response: Response<MapFeedResponse>) {
                    if (response.isSuccessful) {
                        val result = response.body()?.mapFeedList
                        result?.forEach { mapFeed ->
                            val feedId = "FEED:${mapFeed.feedId}"
                            val imgUrl = mapFeed.imgUrl
                            val location = mapFeed.location
                            val (latitudeStr, longitudeStr) = location.split(",")
                            val latitude: Double = latitudeStr.trim().toDouble()
                            val longitude: Double = longitudeStr.trim().toDouble()
                            val latLng = LatLng(latitude, longitude)
                            Log.d("MapFragment", "****FriendFeedMode result : $feedId, $imgUrl, $latLng")

                            if (!::locationProvider.isInitialized) {
                                locationProvider = LocationProvider(requireContext())
                            }
                            if (!::markersMap.isInitialized) {
                                markersMap = mutableMapOf<String, Marker>()
                            }
                            if (!::markerOptionsMap.isInitialized) {
                                markerOptionsMap = mutableMapOf<String, MarkerOptions>()
                            }
                            if (!::markerBitmapMap.isInitialized) {
                                markerBitmapMap = mutableMapOf<String, Bitmap>()
                            }
                            val statusImgUrl = BASIC_IMG_URL
                            val marKerOptions = makeMarkerOptions(feedId, latLng, imgUrl, statusImgUrl)
                            markerOptionsMap[feedId] = marKerOptions

                            val existingMarker = markersMap[feedId]
                            if (existingMarker != null) {
                                animateMarkerTo(existingMarker, marKerOptions.position)
                            } else {
                                val newMarker = mMap.addMarker(marKerOptions)!!
                                markersMap[feedId] = newMarker
                            }
                        }

                        if (result != null) {
                            markersMap.filterKeys { it != currentNickname && !result.any { feed -> "FEED:${feed.feedId}" == it } }
                                .forEach { (nickname, marker) ->
                                    marker.remove()
                                    markersMap.remove(nickname)
                                }
                        }
                        if (result != null) {
                            markerOptionsMap.filterKeys { it != currentNickname && !result.any { feed -> "FEED:${feed.feedId}" == it } }
                                .forEach { (nickname, _) ->
                                    markerOptionsMap.remove(nickname)
                                }
                        }

                        refreshMap(ANIMATE_CAMERA)
                    } else {
                        val errorMessage = JSONObject(response.errorBody()?.string()!!)
                        Log.d("MapFragment", "****FriendFeedMode errorMessage : $errorMessage")
                        Toast.makeText(context, errorMessage.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MapFeedResponse>, t: Throwable) {
                    Log.d("MapFragment", "****FriendFeedMode onFailure : ${t.message}")
                    Toast.makeText(context, "피드 불러오기 실패", Toast.LENGTH_SHORT).show()
                }

            })
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

        binding.fabMoveCurrentLocation.setOnClickListener {
            if (!::locationProvider.isInitialized) {
                locationProvider = LocationProvider(requireContext())
            }
            val location = locationProvider.getLocation()
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                val zoomLevel = 15f
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel)
                mMap.animateCamera(cameraUpdate)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }
}
