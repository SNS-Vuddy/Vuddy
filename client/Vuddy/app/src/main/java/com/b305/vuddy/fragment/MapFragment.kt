package com.b305.vuddy.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentMapBinding
import com.b305.vuddy.extension.getMyLocation
import com.b305.vuddy.extension.logout
import com.b305.vuddy.extension.moveCameraToCurrentLocation
import com.b305.vuddy.extension.renewFriendList
import com.b305.vuddy.extension.setMarker
import com.b305.vuddy.model.FriendLocation
import com.b305.vuddy.model.LocationEvent
import com.b305.vuddy.service.ImmortalLocationService
import com.b305.vuddy.util.SharedManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MapFragment : Fragment(), OnMapReadyCallback {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var isHandlerRunning = false
    var handler = Handler()
    private var runnable = object : Runnable {
        override fun run() {
            setMarker(getMyLocation(), mMap, friendLocationList)
            Log.d("MapFragment", "****run: $latitude, $longitude****")
            handler.postDelayed(this, 5000)
        }
    }

    private val sharedManager: SharedManager by lazy { SharedManager(requireContext()) }
    lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap
    private var friendLocationList = mutableListOf<FriendLocation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        isHandlerRunning = false
        handler.removeCallbacks(runnable)
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
            requireActivity().stopService(Intent(requireContext(), ImmortalLocationService::class.java))
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

        if (isHandlerRunning) {
            isHandlerRunning = false
            handler.removeCallbacks(runnable)
        }
        isHandlerRunning = true
        handler.postDelayed(runnable, 1000)
    }

    override fun onPause() {
        super.onPause()
        isHandlerRunning = false
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        if (isHandlerRunning) {
            isHandlerRunning = false
            handler.removeCallbacks(runnable)
        }
        isHandlerRunning = true
        handler.postDelayed(runnable, 1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isHandlerRunning = false
        handler.removeCallbacks(runnable)
    }
}
