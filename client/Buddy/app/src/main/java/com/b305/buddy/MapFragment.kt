package com.b305.buddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapFragment : Fragment(), OnMapReadyCallback {
    
    private var mMap: GoogleMap? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // 구글맵 쓰려면 SHA-1 인증서랑, 패키지 이름 등록해야함
        // 이 페이지에서 nullpointerexception 발생하면 이거 의심해 봐야함
        // https://console.cloud.google.com/apis/credentials/key/e0832e83-9623-49c5-af00-c04c27585abf?hl=ko&project=airquality-384811
        // 윈도우
        // "C:\Program Files\Android\Android Studio\jre\bin\keytool" -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
        
        val mapFragment = this.childFragmentManager.findFragmentById(R.id.fg_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        
        view.findViewById<ImageView>(R.id.iv_friend).setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_friendFragment)
        }
        view.findViewById<ImageView>(R.id.iv_message).setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_messageFragment)
        }
        view.findViewById<ImageView>(R.id.iv_profile).setOnClickListener {
            it.findNavController().navigate(R.id.action_mapFragment_to_profileFragment)
        }
        
        return view
    }
    
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
}