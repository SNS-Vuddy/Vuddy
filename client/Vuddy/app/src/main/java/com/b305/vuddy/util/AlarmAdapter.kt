package com.b305.vuddy.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.fragment.AlarmFragment
import com.b305.vuddy.model.Alarm
import com.bumptech.glide.Glide


class AlarmAdapter(var alarmList: ArrayList<Alarm>, val callback: AlarmCallback) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    fun setData(alarmList: ArrayList<Alarm>) {
        this.alarmList = alarmList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_alarm_item, parent, false)
        return ViewHolder(view).apply {
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = alarmList[position]

        Glide.with(holder.itemView)
            .load(item.profileImage)
            .into(holder.alarmImage)

        holder.alarmNick.text = item.nickname

        holder.acceptBtn.setOnClickListener {
            callback.onFriendAccept(item.nickname)
        }

        holder.refuseBtn.setOnClickListener {
            callback.onFriendDeny(item.nickname)
        }
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        //        private var view: View = v
        val alarmImage = itemView.findViewById<ImageView>(R.id.friend_alarm_image)
        val alarmNick = itemView.findViewById<TextView>(R.id.friend_alarm_nickname)
        val acceptBtn = itemView.findViewById<Button>(R.id.friend_accept_btn)
        val refuseBtn = itemView.findViewById<Button>(R.id.friend_refuse_btn)

    }

    interface AlarmCallback {
        fun onFriendAccept(friendNickname: String)
        fun onFriendDeny(friendNickname: String)
    }
}
