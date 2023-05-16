package com.b305.vuddy.util

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.bumptech.glide.Glide

class PhotoAdapter(private val items: ArrayList<Uri>, val context: Context) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_img, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(context).load(item)
            .override(500, 500)
            .into(holder.imageViewRecyclerViewImage)
    }
    
    override fun getItemCount(): Int {
        return items.size
    }
    
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var imageViewRecyclerViewImage = v.findViewById<ImageView>(R.id.iv_recycler_view_image)
        
        fun bind(listener: View.OnClickListener, item: String) {
            view.setOnClickListener(listener)
        }
    }
    
}
