package com.b305.vuddy.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.fragment.WriteFeedFragment
import com.b305.vuddy.model.Feeds
import com.bumptech.glide.Glide

class FeedMineAdapter (private val feedsMineList :ArrayList<Feeds>) :
    RecyclerView.Adapter<FeedMineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feeds_item, parent, false)
        return ViewHolder(view).apply {
            itemView.setOnClickListener {

            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = feedsMineList[position]
        Glide.with(holder.itemView)
            .load(item.mainImg)
            .override(300, 300)
            .into(holder.feedimageViewRecyclerView)
    }

    override fun getItemCount(): Int {
        return feedsMineList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
//        private var view: View = v
        val feedimageViewRecyclerView = itemView.findViewById<ImageView>(R.id.iv_recycler_view_feeds)
    }
}
