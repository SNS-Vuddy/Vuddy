package com.b305.vuddy.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentFeedDetailBinding
import com.b305.vuddy.model.FeedResponse
import com.b305.vuddy.model.Feeds
import com.b305.vuddy.model.FeedsResponse
import com.b305.vuddy.util.FeedMineAdapter
import com.b305.vuddy.util.RetrofitAPI
import com.b305.vuddy.util.SlideItemAnimator
import com.b305.vuddy.util.feedDetailImageAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.ThreadContextElement
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedDetailFragment : BottomSheetDialogFragment() {

    private var feedId: Int = 0
    lateinit var binding : FragmentFeedDetailBinding

    private lateinit var feedDetailImageAdapter: feedDetailImageAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            feedId = it.getInt("feedId", 0)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        binding = FragmentFeedDetailBinding.inflate(layoutInflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myFeedTitle : TextView = binding.myfeedTitle
        val myFeednick : TextView = binding.myfeedNickname
        val myFeedContent : TextView = binding.myfeedContent
//        val myFeedImage : ImageView = binding.myfeedImage
        val myFeedLocation : TextView = binding.myfeedLocation
        val myFeedDate : TextView = binding.myfeedDate


        val call = RetrofitAPI.feedService
        call.feedDetailGet(feedId).enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("GET Detail", "get successfully. Response: $result")
                    myFeedTitle.text = result?.data?.title
                    myFeednick.text = result?.data?.nickname
                    myFeedContent.text = result?.data?.content
//                    myFeedLocation.text = result?.data?.location
                    myFeedDate.text = result?.data?.createdAt
                    val feedimageList : List<String> = result?.data?.images!!
                    // 리사이클러뷰
                    val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
//
                    recyclerView = binding.myfeedImage
                    recyclerView.layoutManager = layoutManager

                    feedDetailImageAdapter = feedDetailImageAdapter(feedimageList)
                    recyclerView.adapter = feedDetailImageAdapter

                    // Slide 기능
//                    val itemAnimator = SlideItemAnimator()
//                    recyclerView.itemAnimator = itemAnimator

                } else {
                    Log.d("GET Detail", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Log.e("GET Detail", "get failed.", t)
            }
        })

    }
}
