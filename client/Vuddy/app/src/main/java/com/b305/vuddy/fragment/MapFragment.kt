package com.b305.vuddy.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentMapBinding
import com.b305.vuddy.model.FriendLocation
import com.b305.vuddy.util.SharedManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MapFragment : Fragment(), OnMapReadyCallback, LocationListener {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var isHandlerRunning = false
    var handler = Handler()

    //    private var runnable = object : Runnable {
//        override fun run() {
//            setMarker(getMyLocation(), mMap, friendLocationList)
//            Log.d("MapFragment", "****run: $latitude, $longitude****")
//            handler.postDelayed(this, 5000)
//        }
//    }
    private val sharedManager: SharedManager by lazy { SharedManager(requireContext()) }
    lateinit var binding: FragmentMapBinding
    private lateinit var mMap: GoogleMap
    private var friendLocationList = mutableListOf<FriendLocation>()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        EventBus.getDefault().register(this)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        isHandlerRunning = false
//        handler.removeCallbacks(runnable)
//        EventBus.getDefault().unregister(this)
//    }
//
//    @Subscribe
//    fun onLocationEvent(locationEvent: LocationEvent) {
//        val friendLocation = locationEvent.friendLocation
//        friendLocationList = renewFriendList(friendLocationList, friendLocation)
//    }

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

//        binding.fabLogout.setOnClickListener {
////            requireActivity().stopService(Intent(requireContext(), ImmortalLocationService::class.java))
////            logout(sharedManager)
//            it.findNavController().navigate(R.id.action_mapFragment_to_signupActivity)
//        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initLocationManager()
        setLocationListener()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f))

//
//        moveCameraToCurrentLocation(mMap, friendLocationList)
//
//        val fabMoveCurrentLocation = view?.findViewById<FloatingActionButton>(R.id.fab_move_current_location)
//        fabMoveCurrentLocation?.setOnClickListener {
//            moveCameraToCurrentLocation(mMap, friendLocationList)
//        }
//
//        if (isHandlerRunning) {
//            isHandlerRunning = false
//            handler.removeCallbacks(runnable)
//        }
//        isHandlerRunning = true
//        handler.postDelayed(runnable, 1000)
    }

    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    var count = 0

    private fun initLocationManager() {
        if (!::locationManager.isInitialized) {
            locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    private fun setLocationListener() {
        val minTime = 100L
        val minDistance = 1f
//        lateinit var locationListener: LocationListener
        var gpsLocation: Location? = null
        var networkLocation: Location? = null
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled && !isNetworkEnabled) {
            return
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        if (isGpsEnabled) {
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }

        if (isNetworkEnabled) {
            networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }

        val selectProvider: String
        if (gpsLocation != null && networkLocation != null) {
            if (gpsLocation.accuracy > networkLocation.accuracy) {
                selectProvider = LocationManager.GPS_PROVIDER
            } else {
                selectProvider = LocationManager.NETWORK_PROVIDER
            }
        } else {
            if (gpsLocation != null) {
                selectProvider = LocationManager.GPS_PROVIDER
            } else {
                selectProvider = LocationManager.NETWORK_PROVIDER
            }
        }

        locationListener = this
        locationManager.requestLocationUpdates(selectProvider, minTime, minDistance, locationListener)
    }

    override fun onLocationChanged(location: Location): Unit {
        latitude = location.latitude
        longitude = location.longitude
        val localDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        binding.tvTime.text = localDateTime
        count++
        binding.tvCount.text = count.toString()
        binding.tvLatitude.text = latitude.toString()
        binding.tvLongitude.text = longitude.toString()

        mMap.let {
            it.clear()
            val markerOption = MarkerOptions()
            markerOption.position(LatLng(latitude.toDouble(), longitude.toDouble()))
            it.addMarker(markerOption)
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude.toDouble(), longitude.toDouble()), 18f))
        }
    }

//    override fun onPause() {
//        super.onPause()
//        isHandlerRunning = false
//        handler.removeCallbacks(runnable)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (isHandlerRunning) {
//            isHandlerRunning = false
//            handler.removeCallbacks(runnable)
//        }
//        isHandlerRunning = true
//        handler.postDelayed(runnable, 1000)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        isHandlerRunning = false
//        handler.removeCallbacks(runnable)
//    }
}
