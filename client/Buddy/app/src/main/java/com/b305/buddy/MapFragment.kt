package com.b305.buddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.b305.buddy.databinding.FragmentMapBinding
import com.b305.buddy.util.LocationProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {
    
    lateinit var binding: FragmentMapBinding
    lateinit var mMap: GoogleMap
    
    var currentLat: Double = 37.4979769 // default: 강남역
    var currentLng: Double = 127.027729
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        
        // 임시
        binding.fabSignupTest.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_signupActivity)
        }
        
        binding.ivFriend.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_friendFragment)
        }
        binding.ivMessage.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_messageFragment)
        }
        binding.ivProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_profileFragment)
        }
        
        setButton()
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }
    
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        
        getLocation()
    }
    
    private fun setButton() {
        binding.fabCurrentLocation.setOnClickListener {
            getLocation()
        }
    }
    
    private fun getLocation() {
        val locationProvider = LocationProvider(requireActivity())
        
        currentLat = locationProvider.getLocationLatitude()!!
        currentLng = locationProvider.getLocationLongitude()!!
        
        setMarker()
        
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLat, currentLng), 16f))
    }
    
    private fun setMarker() {
        // 친구 테스트용
        val f1Lat: Double = 36.3507133
        val f1Lng: Double = 127.2986109
        val f2Lat: Double = 36.3599459
        val f2Lng: Double = 127.3051566
        val f3Lat: Double = 36.3616414
        val f3Lng: Double = 127.3575561
        val friend1 = Friend()
        friend1.nickname = 1
        friend1.lat = f1Lat
        friend1.lng = f1Lng
        val friend2 = Friend()
        friend2.nickname = 2
        friend2.lat = f2Lat
        friend2.lng = f2Lng
        val friend3 = Friend()
        friend3.nickname = 3
        friend3.lat = f3Lat
        friend3.lng = f3Lng
        
        val friendList: ArrayList<Friend> = ArrayList()
        friendList.add(friend1)
        friendList.add(friend2)
        friendList.add(friend3)
        
        mMap.let {
            it.clear()
            val markerOption = MarkerOptions()
            markerOption.position(LatLng(currentLat, currentLng))
            val marker = it.addMarker(markerOption)
            
            // 친구 테스트용
            for (friend in friendList) {
                val friendMarkerOption = MarkerOptions()
                friendMarkerOption.position(friend.getLocation())
                val friendMarker = it.addMarker(friendMarkerOption)
            }
        }
    }
    
    inner class Friend {
        var nickname: Long? = null
        var lat: Double? = null
        var lng: Double? = null
        
        fun getLatitude(): Double { // 수정된 이름
            return lat ?: 0.0
        }
        
        fun getLongitude(): Double {
            return lng ?: 0.0
        }
        
        fun getLocation(): LatLng {
            return LatLng(lat!!, lng!!)
        }
    }
}