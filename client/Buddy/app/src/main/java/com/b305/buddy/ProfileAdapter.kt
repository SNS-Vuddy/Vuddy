package com.b305.buddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class ProfileAdapter(private val profileLsit: ArrayList<Profiles>) : RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_list_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = bindingAdapterPosition
                val profile : Profiles = profileLsit.get(curPos)
                Toast.makeText(parent.context, "닉네임: ${profile.name}", Toast.LENGTH_SHORT).show()
                it.findNavController().navigate(R.id.action_friendFragment_to_profileFragment)
            }
        }
    }

    override fun onBindViewHolder(holder: ProfileAdapter.CustomViewHolder, position: Int) {
        val currentItem = profileLsit[position]
        holder.gender.setImageResource(currentItem.gender)
        holder.name.text = currentItem.name
        holder.userId.text = currentItem.userId
    }

    override fun getItemCount(): Int {
        return profileLsit.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gender = itemView.findViewById<ImageView>(R.id.friend_profile)
        val name = itemView.findViewById<TextView>(R.id.friend_name)
        val userId = itemView.findViewById<TextView>(R.id.friend_id)
    }
}