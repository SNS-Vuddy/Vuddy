package com.b305.buddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class FriendFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        
        val view = inflater.inflate(R.layout.fragment_friend, container, false)
        
        view.findViewById<Button>(R.id.btn_map).setOnClickListener {
            it.findNavController().navigate(R.id.action_friendFragment_to_mapFragment)
        }
        
        view.findViewById<Button>(R.id.btn_message).setOnClickListener {
            it.findNavController().navigate(R.id.action_friendFragment_to_messageFragment)
        }
        
        view.findViewById<Button>(R.id.btn_profile).setOnClickListener {
            it.findNavController().navigate(R.id.action_friendFragment_to_profileFragment)
        }
        
        return view
    }
}