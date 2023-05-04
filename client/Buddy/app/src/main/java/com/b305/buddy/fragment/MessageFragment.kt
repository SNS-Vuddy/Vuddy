package com.b305.buddy.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.b305.buddy.R

class MessageFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        
        val view = inflater.inflate(R.layout.fragment_message, container, false)
        
        view.findViewById<ImageView>(R.id.iv_map).setOnClickListener {
            it.findNavController().navigate(R.id.action_messageFragment_to_mapFragment)
        }
        view.findViewById<ImageView>(R.id.iv_write).setOnClickListener {
            it.findNavController().navigate(R.id.action_messageFragment_to_writeFeedFragment)
        }
        view.findViewById<ImageView>(R.id.iv_friend).setOnClickListener {
            it.findNavController().navigate(R.id.action_messageFragment_to_friendFragment)
        }
        
        view.findViewById<ImageView>(R.id.iv_profile).setOnClickListener {
            it.findNavController().navigate(R.id.action_messageFragment_to_profileFragment)
        }
        
        return view
    }
}