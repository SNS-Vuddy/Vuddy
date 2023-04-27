package com.b305.buddy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.b305.buddy.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapFragment : Fragment(), OnMapReadyCallback {
    
    lateinit var binding: FragmentMapBinding
    lateinit var mMap: GoogleMap
    
    var currentLat: Double = 37.4979769 // default: 강남역
    var currentLng: Double = 127.027729
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // 구글맵 쓰려면 SHA-1 인증서랑, 패키지 이름 등록해야함
        
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        
        binding.ivFriend.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_friendFragment)
        }
        binding.ivMessage.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_messageFragment)
        }
        binding.ivProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_profileFragment)
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
        Log.d("MapFragment", "onMapReady")
        
        val locationProvider = LocationProvider(requireActivity())
        val currentLat = locationProvider.getLocationLatitude()
        val currentLng = locationProvider.getLocationLongitude()
        
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLat!!, currentLng!!), 16f))
    }
}