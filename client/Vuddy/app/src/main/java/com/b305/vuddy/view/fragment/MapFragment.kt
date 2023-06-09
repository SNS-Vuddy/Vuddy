package com.b305.vuddy.view.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentMapBinding
import com.b305.vuddy.view.extension.animateMarkerTo
import com.b305.vuddy.view.extension.makeMarkerOptions
import com.b305.vuddy.view.extension.refreshMap
import com.b305.vuddy.model.MapFeedResponse
import com.b305.vuddy.model.User
import com.b305.vuddy.model.UserLocation
import com.b305.vuddy.util.ANIMATE_CAMERA
import com.b305.vuddy.util.BASIC_IMG_URL
import com.b305.vuddy.util.DIALOG_MODE
import com.b305.vuddy.util.FEED_MODE
import com.b305.vuddy.util.FRIEND_FEED_MODE
import com.b305.vuddy.util.location.LocationProvider
import com.b305.vuddy.util.MAP_MODE
import com.b305.vuddy.util.MOVE_CAMERA
import com.b305.vuddy.util.NOT_MOVE_CAMERA
import com.b305.vuddy.service.RetrofitAPI
import com.b305.vuddy.util.SharedManager
import com.b305.vuddy.util.ZOOM_LEVEL
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("NAME_SHADOWING")
class MapFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentMapBinding
    val sharedManager: SharedManager by lazy { SharedManager(requireContext()) }
    private lateinit var locationProvider: LocationProvider
    lateinit var mMap: GoogleMap
    lateinit var markerOptionsMap: MutableMap<String, MarkerOptions>
    lateinit var markersMap: MutableMap<String, Marker>
    lateinit var markerBitmapMap: MutableMap<String, Bitmap>
    var markerMode: Int = MAP_MODE
    private lateinit var currentUser: User
    lateinit var currentNickname: String
    private lateinit var currentProfileImgUrl: String
    private lateinit var currentStatusImgUrl: String
    val friendListBottomSheetFragment = FriendListBottomSheetFragment()


    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("MapFragment", "****onMapReady")
        mMap = googleMap

        if (!::locationProvider.isInitialized) {
            locationProvider = LocationProvider(requireContext())
        }
        if (!::markersMap.isInitialized) {
            markersMap = mutableMapOf()
        }
        if (!::markerOptionsMap.isInitialized) {
            markerOptionsMap = mutableMapOf()
        }
        if (!::markerBitmapMap.isInitialized) {
            markerBitmapMap = mutableMapOf()
        }

        currentUser = sharedManager.getCurrentUser()
        currentNickname = currentUser.nickname!!
        currentProfileImgUrl = currentUser.profileImgUrl!!
        currentStatusImgUrl = currentUser.statusImgUrl!!

        val latitude = locationProvider.getLocationLatitude()!!
        val longitude = locationProvider.getLocationLongitude()!!
        val latLng = LatLng(latitude, longitude)

        addOrUpdateMarker(currentNickname, latLng, currentProfileImgUrl, currentStatusImgUrl)
        refreshMap(MOVE_CAMERA)

        mMap.setOnMarkerClickListener {
            val clickedMarkerNickname = markersMap.entries.find { entry ->
                entry.value == it
            }?.key

            if (clickedMarkerNickname != null && clickedMarkerNickname != currentNickname && !clickedMarkerNickname.startsWith(
                    FEED_PREFIX
                )) {
                val clickedMarkerIcon = markerOptionsMap[clickedMarkerNickname]?.icon
                if (clickedMarkerIcon != null) {
                    val clickedMarkerBitmap = markerBitmapMap[clickedMarkerNickname]!!
                    val friendMarkerBottomSheetFragment =
                        FriendMarkerBottomSheetFragment.newInstance(
                            clickedMarkerNickname,
                            clickedMarkerBitmap
                        )
                    friendMarkerBottomSheetFragment.show(requireActivity().supportFragmentManager, friendMarkerBottomSheetFragment.tag)
                }
            }

            if (clickedMarkerNickname != null && clickedMarkerNickname != currentNickname && clickedMarkerNickname.startsWith(
                    FEED_PREFIX
                )) {
                val feedId = clickedMarkerNickname.split(":")[1].toInt()
                val feedDetailFragment = FeedDetailFragment()
                val bundle = Bundle()
                bundle.putInt("feedId", feedId)
                feedDetailFragment.arguments = bundle
                feedDetailFragment.show(requireActivity().supportFragmentManager, feedDetailFragment.tag)
            }

            if (clickedMarkerNickname == currentNickname) {
                val latLng = it.position
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL)
                mMap.animateCamera(cameraUpdate)
            }
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

        if (!::locationProvider.isInitialized) {
            locationProvider = LocationProvider(requireContext())
        }
        if (!::markersMap.isInitialized) {
            markersMap = mutableMapOf()
        }
        if (!::markerOptionsMap.isInitialized) {
            markerOptionsMap = mutableMapOf()
        }
        if (!::markerBitmapMap.isInitialized) {
            markerBitmapMap = mutableMapOf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onLocationEvent(userLocation: UserLocation) {
        if (markerMode != MAP_MODE && userLocation.nickname != currentNickname) {
            return
        }
        if (markerMode == DIALOG_MODE) {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)



        binding.fabMapMode.setOnClickListener {
            if (markerMode == MAP_MODE) {
                return@setOnClickListener
            }
            Log.d("MapFragment", "****MapMode")
            markerMode = MAP_MODE
            binding.fabMapMode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.selected)
            binding.fabFeedMode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.unselected)
            binding.fabFriendFeedMode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.unselected)

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
            binding.fabMapMode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.unselected)
            binding.fabFeedMode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.selected)
            binding.fabFriendFeedMode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.unselected)

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

                            val statusImgUrl = BASIC_IMG_URL
                            addOrUpdateMarker(feedId, latLng, imgUrl, statusImgUrl)
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
            markerMode = DIALOG_MODE
            friendListBottomSheetFragment.show(childFragmentManager, friendListBottomSheetFragment.tag)
        }

        binding.ivFriend.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_friendFragment)
        }

        binding.ivWrite.setOnClickListener {
            val bottomSheetFragment = WriteFeedFragment()
            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
        }

        binding.ivMessage.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_messageFragment)
        }

        binding.ivProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_profileFragment)
        }

        //TODO: 설정 페이지로 이동
//        binding.fabLogout.setOnClickListener {
//            sharedManager.removeCurrentToken()
//            sharedManager.removeCurrentUser()
//            sharedManager.removeChatRoomList()
//            sharedManager.removeChatList()
//            requireActivity().stopService(Intent(requireContext(), ImmortalService::class.java))
//            it.findNavController().navigate(R.id.action_mapFragment_to_signupActivity)
//        }

        binding.fabMoveCurrentLocation.setOnClickListener {

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

    companion object {
        private const val FEED_PREFIX = "FEED:"
    }

    fun addOrUpdateMarker(feedId: String, latLng: LatLng, imgUrl: String, statusImgUrl: String) {
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
}

