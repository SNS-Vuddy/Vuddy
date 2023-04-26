package com.b305.buddy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        
        view.findViewById<Button>(R.id.btn_map).setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_mapFragment)
        }
        
        view.findViewById<Button>(R.id.btn_friend).setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_friendFragment)
        }
        
        view.findViewById<Button>(R.id.btn_message).setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_messageFragment)
        }
        
        return view
    }
}