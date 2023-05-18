package com.b305.vuddy.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.bumptech.glide.Glide

class FeedDetailImageAdapter(private val feedimageList : List<String>) :
    RecyclerView.Adapter<FeedDetailImageAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed_detail_images, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = feedimageList[position]
        val layoutParams = holder.detailFeedimageViewRecyclerView.layoutParams
        layoutParams.width = 900  // 이미지뷰 너비 조정
        layoutParams.height = 800 // 이미지뷰 높이 조정
        holder.detailFeedimageViewRecyclerView.layoutParams = layoutParams

        Glide.with(holder.itemView)
            .load(item)
//            .override(1000,800)
            .into(holder.detailFeedimageViewRecyclerView)
    }

    override fun getItemCount(): Int {
        return feedimageList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        //        private var view: View = v
        val detailFeedimageViewRecyclerView: ImageView = itemView.findViewById(R.id.iv_recycler_view_feed_images)

    }
}
