package com.b305.vuddy.util.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.view.fragment.FeedDetailFragment
import com.b305.vuddy.model.Feeds
import com.bumptech.glide.Glide

class FeedMineAdapter(private val feedsMineList: ArrayList<Feeds>) :
    RecyclerView.Adapter<FeedMineAdapter.ViewHolder>() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feeds, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = feedsMineList[position]
        val layoutParams = holder.feedimageViewRecyclerView.layoutParams
        layoutParams.width = 330 // 이미지뷰 너비 조정
        layoutParams.height = 330 // 이미지뷰 높이 조정
        holder.feedimageViewRecyclerView.layoutParams = layoutParams

        Glide.with(holder.itemView)
            .load(item.imgUrl)
            .into(holder.feedimageViewRecyclerView)

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("feedId", item.feedId)
            val detailFragment = FeedDetailFragment()
            detailFragment.arguments = bundle

            val fragmentManager = (holder.itemView.context as AppCompatActivity).supportFragmentManager
//            fragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerView, detailFragment)
//                .addToBackStack(null)
//                .commit()
            val transaction = fragmentManager.beginTransaction()
            detailFragment.show(transaction, "FeedDetailBottomSheetFragment")
        }
    }

    override fun getItemCount(): Int {
        return feedsMineList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        //        private var view: View = v
        val feedimageViewRecyclerView: ImageView = itemView.findViewById(R.id.iv_recycler_view_feeds)

    }
}
