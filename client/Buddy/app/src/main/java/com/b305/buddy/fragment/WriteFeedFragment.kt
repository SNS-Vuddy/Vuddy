package com.b305.buddy.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.b305.buddy.R
import com.b305.buddy.databinding.FragmentWriteFeedBinding
import com.b305.buddy.util.PhotoAdapter
import com.b305.buddy.util.RetrofitAPI.feedServie
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WriteFeedFragment : BottomSheetDialogFragment() {

    val deviceSdkVersion = android.os.Build.VERSION.SDK_INT

    lateinit var binding: FragmentWriteFeedBinding
    var photoList = ArrayList<Uri>()
//    val adapter = PhotoAdapter(photoList, requireContext()) // adapter 초기화

    // 요청하고자 하는 권한들
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWriteFeedBinding.inflate(layoutInflater, container, false)

        binding.btnPickPhoto.setOnClickListener {
            openDialog(requireContext())
        }

        binding.btnSaveFeed.setOnClickListener {
//            val feedLocation = binding.etFeedLocation.text.toString()
//            val feedTitle = binding.etFeedTitle.text.toString()
//            val feedContent = binding.etFeedContent.text.toString()

        }

        binding.ivMap.setOnClickListener {
            it.findNavController().navigate(R.id.action_writeFeedFragment_to_mapFragment)
        }

        binding.ivFriend.setOnClickListener {
            it.findNavController().navigate(R.id.action_writeFeedFragment_to_friendFragment)
        }

        binding.ivMessage.setOnClickListener {
            it.findNavController().navigate(R.id.action_writeFeedFragment_to_messageFragment)
        }
        binding.ivProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_writeFeedFragment_to_profileFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (deviceSdkVersion >= 33) {
            requestMultiplePermission.launch(permissionList33)
        } else {
        requestMultiplePermission.launch(permissionList)
        }

        // 리사이클러뷰
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvPhoto.layoutManager = layoutManager
//        binding.rvPhoto.adapter = adapter

        photoList.clear() // photoList 초기화

        val adapter = PhotoAdapter(photoList, requireContext()) // adapter 초기화
        binding.rvPhoto.adapter = adapter
    }

    private fun openDialog(context: Context) {
        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_select_image, null)
        val dialogBuild = AlertDialog.Builder(context).apply {
            setView(dialogLayout)
        }
        val dialog = dialogBuild.create().apply { show() }

        val cameraAddBtn = dialogLayout.findViewById<Button>(R.id.buttonCamera)
        val fileAddBtn = dialogLayout.findViewById<Button>(R.id.buttonGallery)

        fileAddBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.action = Intent.ACTION_PICK
            activityResult.launch(intent)

            dialog.dismiss()
        }

        cameraAddBtn.setOnClickListener {
            // 카메라로 새로운 사진을 찍어서 추가하는 경우의 코드 작성
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(context.packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            context,
                            "com.example.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        activityResultCamera.launch(takePictureIntent)
                    }
                }
            }
            dialog.dismiss()
        }

    }

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
                    result.data?.data?.let { uri ->
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
                sendImage()

            }
        }

    fun sendImage() {
        for (i in 0 until photoList.size) {
            // val imageFile = File(getPathFromUri(photoList[i])) // Uri를 File로 변환하는 메소드
            val imageUri = photoList[i]
            val file = File(getPathFromUri(imageUri))
//            val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("imageFile", file.name, requestFile)

            val call = feedServie.feedImg(body)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val message = response.body()?.string()
                        Log.d("Upload", "Image $i uploaded successfully. Response: $message")
                    } else {
                        Log.d("Upload", "Image $i upload failed. Response: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Upload", "Image $i upload failed.", t)
                }
            })
        }
    }
    private fun getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)!!
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
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
