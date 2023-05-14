package com.b305.vuddy.util

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.fragment.FeedDetailFragment
import com.b305.vuddy.fragment.WriteFeedFragment
import com.b305.vuddy.model.Feeds
import com.b305.vuddy.model.FriendProfile
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class FeedMineAdapter (private val feedsMineList :ArrayList<Feeds>) :
    RecyclerView.Adapter<FeedMineAdapter.ViewHolder>() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feeds_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = feedsMineList[position]
        val layoutParams = holder.feedimageViewRecyclerView.layoutParams
        layoutParams.width = 300 // 이미지뷰 너비 조정
        layoutParams.height = 300 // 이미지뷰 높이 조정
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
        val feedimageViewRecyclerView = itemView.findViewById<ImageView>(R.id.iv_recycler_view_feeds)

    }
}
