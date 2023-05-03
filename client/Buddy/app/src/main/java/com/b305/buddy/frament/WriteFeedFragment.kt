package com.b305.buddy.frament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.b305.buddy.R
import com.b305.buddy.databinding.FragmentMapBinding
import com.b305.buddy.databinding.FragmentWriteFeedBinding

class WriteFeedFragment : Fragment() {
    
    lateinit var binding: FragmentWriteFeedBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        
        binding = FragmentWriteFeedBinding.inflate(layoutInflater, container, false)
        
        binding.ivMap.setOnClickListener {
            it.findNavController().navigate(R.id.action_writeFeedFragment_to_mapFragment)
        }
        
        binding.ivFriend.setOnClickListener {
            it.findNavController().navigate(R.id.action_writeFeedFragment_to_friendFragment)
        }
        
        binding.ivMessage.setOnClickListener {
            it.findNavController().navigate(R.id.action_writeFeedFragment_to_messageFragment)
        }
        binding.ivProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_writeFeedFragment_to_profileFragment)
        }
        
        return binding.root
    }
}