package com.b305.vuddy.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentProfileBinding
import com.b305.vuddy.model.Feeds
import com.b305.vuddy.model.FeedsResponse
import com.b305.vuddy.viewmodel.ProfileViewModel
import com.b305.vuddy.viewmodel.UserDataViewModel
import com.b305.vuddy.model.UserResponse
import com.b305.vuddy.util.adapter.FeedMineAdapter
import com.b305.vuddy.service.RetrofitAPI
import com.b305.vuddy.util.SharedManager
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var feedMineAdapter: FeedMineAdapter
    private lateinit var recyclerView: RecyclerView

    private val deviceSdkVersion = Build.VERSION.SDK_INT

    private var photoList = ArrayList<Uri>()

    private val sharedManager: SharedManager by lazy { SharedManager(requireContext()) }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var userViewModel: UserDataViewModel

    // 요청하고자 하는 권한들
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionList33 = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_MEDIA_IMAGES,
    )
    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    // 권한을 허용하도록 요청
    private val requestMultiplePermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.forEach {
                if (!it.value) {
                    Toast.makeText(requireContext(), "권한 허용 필요", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        userViewModel = ViewModelProvider(this)[UserDataViewModel::class.java]
//        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        userViewModel.userData.observe(viewLifecycleOwner) { userData ->
            val userNick = binding.profileUser
            userNick.text = userData?.data?.nickname

            val profileImageUrl = binding.profileImage
            val profileImage = userData?.data?.profileImage

            if (profileImage != null) {
                Glide.with(requireContext())
                    .load(profileImage)
                    .into(profileImageUrl)
            } else {
                profileImageUrl.setImageResource(R.drawable.bird)
            }

            val user = sharedManager.getCurrentUser()
            user.profileImgUrl = profileImage
            sharedManager.saveCurrentUser(user)
        }

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        viewModel.profile.observe(viewLifecycleOwner) { result ->
            result?.let {
                updateUI(it)
                // 클래스 전역 변수에 데이터를 할당
            }
        }
        binding.ivMap.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_mapFragment)
        }

        binding.ivFriend.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_friendFragment)
        }

        binding.ivMessage.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_messageFragment)
        }

        binding.ivWrite.setOnClickListener {
            val bottomSheetFragment = WriteFeedFragment()
            bottomSheetFragment.show(parentFragmentManager, "bottomSheetTag")
        }

        binding.profileImgchangeBtn.setOnClickListener {
            openDialog(requireContext())
        }

        binding.profileAlarmBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_alarmFragment)
        }

        binding.profileSettingBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_profileFragment_to_settingFragment)
        }


        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    private fun updateUI(data: UserResponse) {

        if (data?.data?.hasNewAlarm == true) {
            binding.newAlarm.visibility = View.VISIBLE
        } else {
            binding.newAlarm.visibility = View.GONE
        }

        val profileImageUrl = binding.profileImage
        val profileImage = data.data.profileImage

        if (profileImage != null) {
            // 프로필 이미지가 있을 경우 이미지 로드 및 표시
            Glide.with(requireContext())
                .load(profileImage)
                .into(profileImageUrl)
        } else {
            // 프로필 이미지가 없을 경우 기본 이미지 표시
            profileImageUrl.setImageResource(R.drawable.bird)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (deviceSdkVersion >= 33) {
            requestMultiplePermission.launch(permissionList33)
        } else {
            requestMultiplePermission.launch(permissionList)
        }
        //Todo
//        viewModel.profile.observe(this) { result ->
        viewModel.profile.observe(viewLifecycleOwner) { result ->
            result?.let {
                updateUI(it)
                // 클래스 전역 변수에 데이터를 할당
            }
        }

        viewModel.loadProfile()
        userViewModel.loadUserData()

        val call = RetrofitAPI.feedService
        call.feedMineGet().enqueue(object : Callback<FeedsResponse> {
            override fun onResponse(call: Call<FeedsResponse>, response: Response<FeedsResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("GET All", "get successfully. Response: $result")
                    val feedList: ArrayList<Feeds> = result?.FeedList!!

                    // 리사이클러뷰
//                    val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    //격자 레이아웃
                    val gridLayoutManager = GridLayoutManager(context, 3)
                    // 역순 작업
                    // 1번
//                    gridLayoutManager.reverseLayout = true
//                  // 2번
//                    gridLayoutManager.setReverseLayout(true)

                    // 3번
                    feedList.reverse()

                    recyclerView = binding.feedsMineList
                    recyclerView.layoutManager = gridLayoutManager

                    feedMineAdapter = FeedMineAdapter(feedList)
                    recyclerView.adapter = feedMineAdapter

                    viewModel.loadProfile()
                } else {
                    Log.d("GET All", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FeedsResponse>, t: Throwable) {
                Log.e("GET All", "get failed.", t)
            }
        })
        val usercall = RetrofitAPI.userService
        usercall.userDataGet().enqueue(object : Callback<UserResponse> {
            @SuppressLint("ResourceAsColor")
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("UserData Get", "get successfully. Response: $result")
                    val userNick = binding.profileUser
                    userNick.text = result?.data?.nickname
                    val alarmBtn = binding.profileAlarmBtn
                    if (result?.data?.hasNewAlarm == true) {
                        binding.newAlarm.visibility = View.VISIBLE
                    } else {
                        binding.newAlarm.visibility = View.GONE
                    }

                    // 프로필 이미지
                    val profileImageUrl = binding.profileImage
                    val profileImage = result?.data?.profileImage

                    if (profileImage != null) {
                        // 프로필 이미지가 있을 경우 이미지 로드 및 표시
                        Glide.with(requireContext())
                            .load(profileImage)
                            .into(profileImageUrl)
                    } else {
                        // 프로필 이미지가 없을 경우 기본 이미지 표시
                        profileImageUrl.setImageResource(R.drawable.bird)
                    }
                    val user = sharedManager.getCurrentUser()
                    user.profileImgUrl = profileImage
                    sharedManager.saveCurrentUser(user)
                    viewModel.loadProfile()

                } else {
                    Log.d("UserData Get", "get failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("UserData Get", "get failed.")
            }
        })
    }

    private fun openDialog(context: Context) {
        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_img_change, null)
        val dialogBuild = AlertDialog.Builder(context).apply {
            setView(dialogLayout)
        }
        val dialog = dialogBuild.create().apply { show() }

        val imgChangeBtn = dialogLayout.findViewById<AppCompatImageView>(R.id.profile_change_camerabtn)

        imgChangeBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"
//            intent.data = MediaStore.Images.Media.CONTENT_TYPE
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.action = Intent.ACTION_PICK
            intent.action = Intent.ACTION_GET_CONTENT;
            activityResult.launch(intent)

        }
        val imgChangeCompleteBtn = dialogLayout.findViewById<Button>(R.id.profile_changeimg_complete_btn)
        imgChangeCompleteBtn.setOnClickListener {
            imgChange()
            dialog.dismiss()
        }

    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                photoList.clear()

                result.data?.data?.let {
                    val imageUri: Uri? = result.data?.data
                    if (imageUri != null) {
                        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_img_change, null)
                        val profileImageUrl = dialogLayout.findViewById<CircleImageView>(R.id.profile_change_box)
                        Glide.with(this)
                            .load(imageUri)
                            .into(profileImageUrl)

                        photoList.add(imageUri)
                    }
                }
            }
        }
    private fun imgChange() {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        // Uri를 File로 변환하는 메소드
        val imageUri = photoList[0]
        val file = File(getPathFromUri(imageUri))
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        builder.addFormDataPart("images", file.name, requestFile)

        val body = builder.build().parts

        val call = RetrofitAPI.userService.profileImgChange(body)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val message = response.body()?.string()
                    Log.d("프로필 교체성공", "All uploaded successfully. Response: $message")


                    viewModel.loadProfile()
                } else {
                    Log.d("프로필 교체 실패", "upload failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("프로필 교체 실패", "upload failed.", t)
            }
        })
    }

    @SuppressLint("Recycle")
    private fun getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)!!
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }


}

