package com.b305.buddy.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.b305.buddy.R
import com.b305.buddy.databinding.FragmentMapBinding
import com.b305.buddy.extension.getMyLocation
import com.b305.buddy.extension.logout
import com.b305.buddy.extension.moveCameraToCurrentLocation
import com.b305.buddy.extension.renewFriendList
import com.b305.buddy.extension.setMarker
import com.b305.buddy.model.FriendLocation
import com.b305.buddy.model.LocationEvent
import com.b305.buddy.util.SharedManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * 1. 실행 전
 * 10인 친구 자료구조 생성
 * 2. 실행
 * 내 위치 마커 추가
 * 카메라 위치 변경
 * 카메라 줌 변경
 * 3. 마커 수정해야 하는 상황
 * 인터벌 1초
 * 친구 위치 업데이트
 * 4. 마커 수정
 * 내 위치 + 10인 친구 자료구조 반영
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    private val sharedManager: SharedManager by lazy { SharedManager(requireContext()) }
    private lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap
    private var friendLocationList = mutableListOf<FriendLocation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)

    }

    @Subscribe
    fun onLocationEvent(locationEvent: LocationEvent) {
        val friendLocation = locationEvent.friendLocation
        friendLocationList = renewFriendList(friendLocationList, friendLocation)
    }

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
            logout(sharedManager)
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
        moveCameraToCurrentLocation(mMap, friendLocationList)

        val fabMoveCurrentLocation = view?.findViewById<FloatingActionButton>(R.id.fab_move_current_location)
        fabMoveCurrentLocation?.setOnClickListener {
            moveCameraToCurrentLocation(mMap, friendLocationList)
        }

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                setMarker(getMyLocation(), mMap, friendLocationList)
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)
    }
}
