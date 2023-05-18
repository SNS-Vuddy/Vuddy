package com.b305.vuddy.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.b305.vuddy.databinding.FragmentCommentBinding
import com.b305.vuddy.viewmodel.FeedDetailViewModel
import com.b305.vuddy.model.FeedResponse
import com.b305.vuddy.util.adapter.CommentAdapter
import com.b305.vuddy.service.RetrofitAPI
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentFragment(private val feedResponse: FeedResponse) : BottomSheetDialogFragment() {

    private lateinit var viewModel: FeedDetailViewModel
    private var feedId = feedResponse.data.feedId

    private val commentList = feedResponse.data.comments
//    private val userData = feedResponse.data.
    private var rvAdapter = CommentAdapter(commentList)

    private val binding by lazy { FragmentCommentBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.commentBackBtn.setOnClickListener {
            dismiss()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
//        binding = FragmentCommentBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[FeedDetailViewModel::class.java]
        viewModel.feedDetail.observe(this) { result ->
            result?.let {
                updateUI(it)
            }
        }
        return binding.root
    }

    private fun updateUI(data: FeedResponse) {
        val comments = data.data.comments.toMutableList()
        rvAdapter.updateData(comments)

        binding.commentBtn.setOnClickListener {
            sendComment()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadFeedDetail(feedId)

// 코멘트 Recyclerview
        val recyclerview = binding.rvComment
//        val rvAdapter = CommentAdapter(commentList)
        recyclerview.adapter = rvAdapter

        recyclerview.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun sendComment(){
        val feedId = feedResponse.data.feedId
        val commentContent = binding.commentInputText.text.toString()
//        val RequestComment = comment_content.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestBody =
            "{\"comment\":\"$commentContent\"}".toRequestBody("application/json".toMediaTypeOrNull())
        val call = RetrofitAPI.feedService.commentWrite(feedId, requestBody)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("댓글 작성 성공", "get successfully. Response: $result")
//                    var commentss = feedResponse.data.comments
//                    rvAdapter.updateData(commentss)
                    viewModel.loadFeedDetail(feedId)
//                    feedResponse.data.comments = commentList.toMutableList()
//                    rvAdapter.updateData(feedResponse.data.comments)
                } else {
                    Log.d("댓글 작성 실패", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("댓글 작성 실패", "get failed.", t)
            }
        })
    }
}

