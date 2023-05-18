package com.b305.vuddy.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentWriteFeedBinding
import com.b305.vuddy.model.FeedResponse
import com.b305.vuddy.util.location.LocationProvider
import com.b305.vuddy.util.adapter.PhotoAdapter
import com.b305.vuddy.service.RetrofitAPI.feedService
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WriteFeedFragment : BottomSheetDialogFragment() {

    private val deviceSdkVersion = Build.VERSION.SDK_INT

    lateinit var binding: FragmentWriteFeedBinding
    private var photoList = ArrayList<Uri>()

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWriteFeedBinding.inflate(layoutInflater, container, false)

        binding.btnPickPhoto.setOnClickListener {
            openDialog(requireContext())
        }

        binding.btnSaveFeed.setOnClickListener {
            sendImage()
            dismiss()
        }

        getFeedDetail()


//        binding.ivMap.setOnClickListener {
//            it.findNavController().navigate(R.id.action_writeFeedFragment_to_mapFragment)
//        }
//
//        binding.ivFriend.setOnClickListener {
//            it.findNavController().navigate(R.id.action_writeFeedFragment_to_friendFragment)
//        }
//
//        binding.ivMessage.setOnClickListener {
//            it.findNavController().navigate(R.id.action_writeFeedFragment_to_messageFragment)
//        }
//        binding.ivProfile.setOnClickListener {
//            it.findNavController().navigate(R.id.action_writeFeedFragment_to_profileFragment)
//        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LinearLayout>(R.id.wirte_feed_layout).background = ContextCompat.getDrawable(requireContext(), R.drawable.shape_bottom_sheet)

        if (deviceSdkVersion >= 33) {
            requestMultiplePermission.launch(permissionList33)
        } else {
            requestMultiplePermission.launch(permissionList)
        }

        // 리사이클러뷰
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvPhoto.layoutManager = layoutManager

        photoList.clear() // photoList 초기화

        val adapter = PhotoAdapter(photoList, requireContext()) // adapter 초기화
        binding.rvPhoto.adapter = adapter
    }

    @SuppressLint("InflateParams")
    private fun openDialog(context: Context) {
        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_select_image, null)
        val dialogBuild = AlertDialog.Builder(context).apply {
            setView(dialogLayout)
        }
        val dialog = dialogBuild.create().apply { show() }

//        val cameraAddBtn = dialogLayout.findViewById<Button>(R.id.buttonCamera)
        val fileAddBtn = dialogLayout.findViewById<Button>(R.id.buttonGallery)

        fileAddBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"
//            intent.data = MediaStore.Images.Media.CONTENT_TYPE
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_PICK
            activityResult.launch(intent)

            dialog.dismiss()
        }
    }

//        cameraAddBtn.setOnClickListener {
//            // 카메라로 새로운 사진을 찍어서 추가하는 경우의 코드 작성
//            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//                takePictureIntent.resolveActivity(context.packageManager)?.also {
//                    val photoFile: File? = try {
//                        createImageFile()
//                    } catch (ex: IOException) {
//                        // Error occurred while creating the File
//                        null
//                    }
//                    // Continue only if the File was successfully created
//                    photoFile?.also {
//                        val photoURI: Uri = FileProvider.getUriForFile(
//                            context,
//                            "com.example.android.fileprovider",
//                            it
//                        )
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                        activityResultCamera.launch(takePictureIntent)
//                    }
//                }
//            }
//            dialog.dismiss()
//        }
//
//    }

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                photoList.clear()

                if (result.data?.clipData != null) { // 사진 여러개 선택한 경우
                    val count = result.data?.clipData!!.itemCount
                    if (count > 10) {
                        Toast.makeText(requireContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                            .show()

                    }
                    for (i in 0 until count) {
                        val imageUri = result.data?.clipData!!.getItemAt(i).uri
                        photoList.add(imageUri)
                    }

                } else { // 단일 선택
                    result.data?.data?.let {
                        val imageUri: Uri? = result.data?.data
                        if (imageUri != null) {
                            Toast.makeText(requireContext(), "${imageUri::class.simpleName}", Toast.LENGTH_LONG).show()
                            photoList.add(imageUri)
                        }
                    }
                }
                // adapter.notifyDataSetChanged()
                // 어댑터 생성
                val adapter = PhotoAdapter(photoList, requireContext())
                binding.rvPhoto.adapter = adapter
            }
        }

    private fun getMyLocation(): String {
        val locationProvider = LocationProvider(requireActivity())
        val latitude = locationProvider.getLocationLatitude()!!
        val longitude = locationProvider.getLocationLongitude()!!
        return "$latitude, $longitude"  // 36.35528344545768,127.29783622867265
    }
    private fun sendImage() {
        // 이미지 보내는 코드
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        for (i in 0 until photoList.size) {
            // Uri를 File로 변환하는 메소드
            val imageUri = photoList[i]
            val file = File(getPathFromUri(imageUri))
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

            builder.addFormDataPart("images", file.name, requestFile)
        }
        val body = builder.build().parts

        // title, content, location, tags 등 보내는 코드

        val title = binding.etFeedTitle.text.toString()
        val content = binding.etFeedContent.text.toString() // content EditText에서 문자열을 가져옴
        val location = getMyLocation()
        val tags = ArrayList<RequestBody>() // 추출된 태그를 저장할 List

        val requestBodytitle = title.toRequestBody("text/plain".toMediaTypeOrNull())
//        val content = RequestBody.create(MediaType.parse("text/plain"), "yourContent")
        val requestBodylocation = location.toRequestBody("text/plain".toMediaTypeOrNull())

        // content 문자열에서 "@" 문자열이 있는지 확인하고, 있다면 추출하여 tags List에 추가
//        while (content.contains("@")) {
//            val index = content.indexOf("@") // "@" 문자열의 위치를 찾음
//            val subString = content.substring(index + 1) // "@" 이후의 문자열을 추출
//            val tag = subString.split(" ")[0] // 첫번째 단어를 추출하여 태그로 사용
//            val RequestBodytag = tag.toRequestBody("text/plain".toMediaTypeOrNull())
//            tags.add(RequestBodytag) // 태그를 List에 추가
////            tags.add(MultipartBody.Part.createFormData("tag", tag))
//            content = subString // 추출한 문자열을 제외한 나머지 문자열을 다시 처리하기 위해 content 변수를 업데이트
//
//            // 태그를 클릭했을 때 처리할 코드
//            val tagSpan = object : ClickableSpan() {
//                override fun onClick(view: View) {
//                    // 태그 클릭 시 처리할 내용 입력
//                    // 예를 들어 해당 태그의 페이지로 이동하도록 구현할 수 있음
//                    val intent = Intent(context, TagActivity::class.java)
//                    intent.putExtra("tag", tag)
//                    context?.startActivity(intent)
//                }
//            }
//            val startIndex = index // 태그의 시작 위치
//            val endIndex = index + tag.length + 1 // 태그의 끝 위치
//            val spannableString = SpannableString(content) // 태그를 클릭할 수 있는 SpannableString 생성
//            spannableString.setSpan(tagSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//            val editable = SpannableStringBuilder(spannableString)
//            binding.etFeedContent.text =
//                editable // 태그를 클릭할 수 있는 SpannableString으로 content EditText의 텍스트를 설정
//        }
        val requestBodyContent = content.toRequestBody("text/plain".toMediaTypeOrNull())

        val call = feedService.feedWrite(requestBodytitle, requestBodyContent, requestBodylocation, tags, body)
//        val call = feedServie.feedImg(body)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val message = response.body()?.string()
                    Log.d("Upload", "All uploaded successfully. Response: $message")
                } else {
                    Log.d("Upload", "upload failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Upload", "upload failed.", t)
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

    private fun getFeedDetail() {
        val call = feedService.feedDetailGet(26)
        call.enqueue(object : Callback<FeedResponse> {
            override fun onResponse(call: Call<FeedResponse>, response: Response<FeedResponse>) {
                if (response.isSuccessful) {
//                val user = response.body()
                    val message = response.body()?.data
                    Log.d("GET api", "get successfully. Response: $message")
                    // 사용자 객체 사용
                } else {
                    Log.d("GET api", "get failed. Response: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                // 요청 실패 처리
                Log.e("GET api", "get failed.", t)
            }
        })
    }

    private val activityResultCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val photoFile = currentPhotoPath?.let { File(it) }
                photoFile?.let {
                    photoList.add(Uri.fromFile(it))
                    // adapter.notifyDataSetChanged()
                    // 어댑터 생성
                    val adapter = PhotoAdapter(photoList, requireContext())
                    binding.rvPhoto.adapter = adapter
                }
            } else {
                currentPhotoPath?.let {
                    val file = File(it)
                    if (file.exists()) {
                        file.delete()
                    }
                }
            }
        }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    // 사진 경로 저장
    private var currentPhotoPath: String? = null
}
