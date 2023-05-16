package com.b305.vuddy.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.fragment.FriendProfileFragment
import com.b305.vuddy.model.Comment
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter (private var CommentList : List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    fun updateData(newData: List<Comment>) {
        CommentList = newData.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
       val context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = CommentList[position]

        holder.CommentUser.text = item.nickname
        holder.CommentContent.text = item.content
        // 프로필 이미지
        val profileImageUrl = holder.CommentUserImage
        val profileImage = item.profileImg

        if (profileImage != null) {
            // 프로필 이미지가 있을 경우 이미지 로드 및 표시
            Glide.with(holder.itemView)
                .load(profileImage)
                .into(profileImageUrl)
        } else {
            // 프로필 이미지가 없을 경우 기본 이미지 표시
            profileImageUrl.setImageResource(R.drawable.man)
        }

        holder.CommentUserImage.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("nickname", item.nickname)
            val friendProfileFragment = FriendProfileFragment()
            friendProfileFragment.arguments = bundle

            val activity = holder.itemView.context as? AppCompatActivity
            activity?.let {
                val fragmentManager = it.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, friendProfileFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }


    override fun getItemCount(): Int {
        return CommentList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val CommentUserImage: CircleImageView = itemView.findViewById(R.id.comment_profile_image)
        val CommentUser: TextView = itemView.findViewById(R.id.comment_user)
        val CommentContent: TextView = itemView.findViewById(R.id.comment_content)

        init {
            CommentUserImage.setOnClickListener {
                val item = CommentList[adapterPosition]
                val bundle = Bundle()
                bundle.putString("nickname", item.nickname)
                val friendprofileFragment = FriendProfileFragment()
                friendprofileFragment.arguments = bundle

                val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, friendprofileFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
