package com.b305.vuddy.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.model.Comment

class CommentAdapter (private var CommentList : List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

//    fun updateData(newComments: List<Comment>) {
//        CommentList = newComments.toMutableList()
//        notifyDataSetChanged()
//    }

    fun updateData(newData: List<Comment>) {
        CommentList = newData.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = CommentList[position]

        holder.CommentUse.text = item.nickname
        holder.CommentContent.text = item.content

    }

    override fun getItemCount(): Int {
        return CommentList.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val CommentUserImage = itemView.findViewById<ImageView>(R.id.comment_profile_image)
        val CommentUse = itemView.findViewById<TextView>(R.id.comment_user)
        val CommentContent = itemView.findViewById<TextView>(R.id.comment_content)

    }

}
