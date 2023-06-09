package com.b305.vuddy.view.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.b305.vuddy.R
import com.b305.vuddy.view.activity.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

@Suppress("DEPRECATION")
class FriendMarkerBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_marker_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nickname = arguments?.getString(ARG_NICKNAME)
        val iconBitmap = arguments?.getParcelable<Bitmap>(ARG_ICON_BITMAP)
        super.onViewCreated(view, savedInstanceState)
        val imageViewProfileImg: ImageView = view.findViewById(R.id.iv_profile_img)
        val textViewNickname: TextView = view.findViewById(R.id.tv_profile_nickname)

        if (iconBitmap != null) {
            imageViewProfileImg.setImageBitmap(iconBitmap)
        }

        textViewNickname.text = nickname


        val buttonGoToProfile = view.findViewById<Button>(R.id.btn_go_to_profile)
        buttonGoToProfile.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("nickname", nickname)
            val friendProfileFragment = FriendProfileFragment()
            friendProfileFragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, friendProfileFragment)
                .addToBackStack(null)
                .commit()
            dismiss()
        }

        val buttonGoToChatting = view.findViewById<Button>(R.id.btn_send_message)
        buttonGoToChatting.setOnClickListener {
            val service = (requireContext() as MainActivity).getServiceInstance()
            val chatSocket = service?.getChatSocket()
            chatSocket?.goChatting(nickname)
            val bundle = Bundle()
            bundle.putString("nickname", nickname)
            val chatFragment = ChatFragment()
            chatFragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, chatFragment)
                .addToBackStack(null)
                .commit()
            dismiss()
        }
    }

    companion object {
        private const val ARG_NICKNAME = "nickname"
        private const val ARG_ICON_BITMAP = "icon_bitmap"

        fun newInstance(nickname: String, icon: Bitmap): FriendMarkerBottomSheetFragment {
            val args = Bundle()
            args.putString(ARG_NICKNAME, nickname)
            args.putParcelable(ARG_ICON_BITMAP, icon)
            val fragment = FriendMarkerBottomSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
