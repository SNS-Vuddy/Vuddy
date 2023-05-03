package com.b305.buddy.frament

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.b305.buddy.R
import com.b305.buddy.databinding.FragmentWriteFeedBinding
import com.b305.buddy.util.PhotoAdapter

class WriteFeedFragment : Fragment() {

    lateinit var binding: FragmentWriteFeedBinding
    //private val GALLERY_REQUEST_CODE = 200
    //var photoList = ArrayList<Uri>()
    //val adapter = PhotoAdapter(photoList, requireContext())

    //// 요청하고자 하는 권한들
    //private val permissionList = arrayOf(
    //    Manifest.permission.CAMERA,
    //    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    //    Manifest.permission.READ_EXTERNAL_STORAGE
    //)
    //
    //// 권한을 허용하도록 요청
    //private val requestMultiplePermission =
    //    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
    //        results.forEach {
    //            if (!it.value) {
    //                Toast.makeText(requireContext(), "권한 허용 필요", Toast.LENGTH_SHORT).show()
    //                requireActivity().finish()
    //            }
    //        }
    //    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentWriteFeedBinding.inflate(layoutInflater, container, false)

        binding.btnPickPhoto.setOnClickListener {
            //openDialog(requireContext())
        }

        binding.btnSaveFeed.setOnClickListener {
            val feedLocation = binding.etFeedLocation.text.toString()
            val feedTitle = binding.etFeedTitle.text.toString()
            val feedContent = binding.etFeedContent.text.toString()
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
    
    //override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //    super.onViewCreated(view, savedInstanceState)
    //    requestMultiplePermission.launch(permissionList)
    //
    //    // 리사이클러뷰
    //    val layoutManager = LinearLayoutManager(requireContext())
    //    binding.rvPhoto.layoutManager = layoutManager
    //    binding.rvPhoto.adapter = adapter
    //}
    //
    //private fun openDialog(context: Context) {
    //    val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_select_image, null)
    //    val dialogBuild = AlertDialog.Builder(context).apply {
    //        setView(dialogLayout)
    //    }
    //    val dialog = dialogBuild.create().apply { show() }
    //
    //    val cameraAddBtn = dialogLayout.findViewById<Button>(R.id.buttonCamera)
    //    val fileAddBtn = dialogLayout.findViewById<Button>(R.id.buttonGallery)
    //
    //    fileAddBtn.setOnClickListener {
    //        val intent = Intent(Intent.ACTION_PICK)
    //
    //        intent.type = "image/*"
    //        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    //        intent.action = Intent.ACTION_PICK
    //        activityResult.launch(intent)
    //
    //        dialog.dismiss()
    //    }
    //
    //    cameraAddBtn.setOnClickListener {
    //        // 카메라로 새로운 사진을 찍어서 추가하는 경우의 코드 작성
    //    }
    //}
    //
    //private val activityResult =
    //    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    //        if (result.resultCode == RESULT_OK) {
    //            photoList.clear()
    //
    //            if (result.data?.clipData != null) { // 사진 여러개 선택한 경우
    //                val count = result.data?.clipData!!.itemCount
    //                if (count > 10) {
    //                    Toast.makeText(requireContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG)
    //                        .show()
    //
    //                }
    //                for (i in 0 until count) {
    //                    val imageUri = result.data?.clipData!!.getItemAt(i).uri
    //                    photoList.add(imageUri)
    //                }
    //
    //            } else { // 단일 선택
    //                result.data?.data?.let { uri ->
    //                    val imageUri: Uri? = result.data?.data
    //                    if (imageUri != null) {
    //                        photoList.add(imageUri)
    //                    }
    //                }
    //            }
    //            adapter.notifyDataSetChanged()
    //
    //        }
    //    }
}