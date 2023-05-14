package com.b305.vuddy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.b305.vuddy.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FeedMarkerBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val feedId = arguments?.getInt(ARG_FEED_ID)
        Toast.makeText(context, "$feedId", Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.fragment_feed_marker_bottom_sheet, container, false)
    }

    companion object {
        private const val ARG_FEED_ID = "feed_id"

        fun newInstance(feedId: Int): FeedMarkerBottomSheetFragment {
            val args = Bundle()
            args.putInt(ARG_FEED_ID, feedId)
            val fragment = FeedMarkerBottomSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
