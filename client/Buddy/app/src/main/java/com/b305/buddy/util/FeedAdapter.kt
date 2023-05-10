package com.b305.buddy.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.b305.buddy.R

class FeedAdapter (val List : MutableList<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return List.size
    }

    override fun getItem(position: Int): Any {
        return List[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView

        if(convertView == null ) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.feeds_item,parent, false)
        }

        val title = convertView!!.findViewById<TextView>(R.id.iv_recycler_view_feeds)
        title.text = List[position]

        return convertView!!
    }
}
