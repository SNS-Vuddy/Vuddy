package com.b305.vuddy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentFeedDetailBinding
import com.b305.vuddy.model.FeedData
import com.b305.vuddy.model.FeedDetailViewModel
import com.b305.vuddy.model.FeedResponse
import com.b305.vuddy.util.RetrofitAPI
import com.b305.vuddy.util.SlideItemAnimator
import com.b305.vuddy.util.feedDetailImageAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.ResponseBody
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedDetailFragment : BottomSheetDialogFragment() {

    private var feedId: Int = 0
//    private val binding by lazy { FragmentFeedDetailBinding.inflate(layoutInflater) }
    private lateinit var binding: FragmentFeedDetailBinding
    private lateinit var viewModel: FeedDetailViewModel
    private lateinit var data: FeedResponse

    private lateinit var feedDetailImageAdapter: feedDetailImageAdapter
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentFeedDetailBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(FeedDetailViewModel::class.java)
        viewModel.feedDetail.observe(this) { result ->
            result?.let {
                data = it
                updateUI(it)
                // 클래스 전역 변수에 데이터를 할당
            }
        }

        binding.myfeedLikeFalse.setOnClickListener {
            FeedLikeCount()
        }
        binding.myfeedLikeTrue.setOnClickListener {
            FeedLikeCount()
        }

        binding.myfeedComment.setOnClickListener{
            val bottomSheetFragment = CommentFragment(data)
            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
        }

        return binding.root
    }

    private fun updateUI(data: FeedResponse) {
        binding.myfeedTitle.text = data.data.title
        binding.myfeedNickname.text = data.data.nickname
        binding.myfeedContent.text = data.data.content
        binding.myfeedDate.text = data.data.createdAt

        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.myfeedImage.layoutManager = layoutManager
        val feedimageList: List<String> = data.data.images

        feedDetailImageAdapter = feedDetailImageAdapter(feedimageList)
        binding.myfeedImage.adapter = feedDetailImageAdapter

        binding.myfeedComment.setOnClickListener{
            val bottomSheetFragment = CommentFragment(data)
            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
        }

        binding.myfeedLikeCount.text = data.data.likesCount.toString()
        binding.myfeedLikeTrue.isSelected = data.data.isLiked
        binding.myfeedLikeFalse.isSelected = !data.data.isLiked
        if (data.data.isLiked) {
            binding.myfeedLikeTrue.visibility = View.VISIBLE
            binding.myfeedLikeFalse.visibility = View.GONE
        }else {
            binding.myfeedLikeTrue.visibility = View.GONE
            binding.myfeedLikeFalse.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            feedId = it.getInt("feedId", 0)
        }
        viewModel.feedDetail.observe(viewLifecycleOwner) { result ->
            result?.let {
                updateUI(it)
                data = it
            }
        }
        viewModel.loadFeedDetail(feedId)

        val myFeedTitle : TextView = binding.myfeedTitle
        val myFeednick : TextView = binding.myfeedNickname
        val myFeedContent : TextView = binding.myfeedContent
//        val myFeedImage : ImageView = binding.myfeedImage
        val myFeedLocation : TextView = binding.myfeedLocation
        val myFeedDate : TextView = binding.myfeedDate
        val myFeedcommetCount = binding.myfeedComment

        val call = RetrofitAPI.feedService
        call.feedDetailGet(feedId).enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
                    val feedresult = response.body()
                    Log.d("GET Detail", "get successfully. Response: $feedresult")
                    myFeedTitle.text = feedresult?.data?.title
                    myFeednick.text = feedresult?.data?.nickname
                    myFeedContent.text = feedresult?.data?.content
//                    myFeedLocation.text = result?.data?.location
                    myFeedDate.text = feedresult?.data?.createdAt
                    myFeedcommetCount.text = "댓글 ${feedresult?.data?.commentsCount}개"
                    binding.myfeedLikeCount.text = feedresult?.data?.likesCount.toString()

                    // 이미지 리사이클러 뷰
                    val feedimageList : List<String> = feedresult?.data?.images!!
                    val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
//
                    recyclerView = binding.myfeedImage
                    recyclerView.layoutManager = layoutManager

                    feedDetailImageAdapter = feedDetailImageAdapter(feedimageList)
                    recyclerView.adapter = feedDetailImageAdapter

                    // Slide 기능
//                    val itemAnimator = SlideItemAnimator()
//                    recyclerView.itemAnimator = itemAnimator

                    //
                    binding.myfeedComment.setOnClickListener{
//            it.findNavController().navigate(R.id.action_feedDetailFragment_to_commentFragment)
                        val bottomSheetFragment = CommentFragment(feedresult)
                        bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
                    }
                    viewModel.loadFeedDetail(feedId)
                } else {
                    Log.d("GET Detail", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                Log.e("GET Detail", "get failed.", t)
            }
        })
    }

    fun FeedLikeCount() {
        val data = viewModel.feedDetail.value ?: return
        val call = RetrofitAPI.feedService.feedLike(data.data.feedId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("좋아요 시도 성공", "get successfully. Response: $result")
//                    if (data.data.isLiked) {
//                        binding.myfeedLikeTrue.visibility = View.GONE
//                        binding.myfeedLikeFalse.visibility = View.VISIBLE
//                    }else {
//                        binding.myfeedLikeTrue.visibility = View.VISIBLE
//                        binding.myfeedLikeFalse.visibility = View.GONE
//                    }
                    viewModel.loadFeedDetail(feedId) // 데이터를 다시 새로고침
                } else {
                    Log.d("좋아요 시도 실패", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("좋아요 시도 실패", "get failed.", t)
            }
        })
        }

}
