package com.b305.buddy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class MapFragment : Fragment() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // 구글맵 쓰려면 SHA-1 인증서랑, 패키지 이름 등록해야함
        
        // navigation
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        
        // 임시 이동 버튼
        view.findViewById<Button>(R.id.btn_move_to_map).setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivity(intent)
        }
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
}