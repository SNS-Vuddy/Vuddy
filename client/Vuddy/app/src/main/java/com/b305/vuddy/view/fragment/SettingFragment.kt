package com.b305.vuddy.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.b305.vuddy.R
import com.b305.vuddy.databinding.FragmentSettingBinding
import com.b305.vuddy.service.ImmortalService
import com.b305.vuddy.util.SharedManager
import com.b305.vuddy.viewmodel.UserDataViewModel

@Suppress("DEPRECATION")
class SettingFragment : Fragment() {

    private lateinit var binding : FragmentSettingBinding
    val sharedManager :SharedManager by lazy { SharedManager(requireContext()) }
//    private lateinit var userData: UserResponse

    private lateinit var userViewModel : UserDataViewModel

//    val userData = arguments?.getParcelable<UserResponse>("userData")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)

        binding.settingBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }


        // 토글 버튼이 있는 뷰 설정
//        val toggleButton = binding.feedsSwitch

        userViewModel = ViewModelProvider(this)[UserDataViewModel::class.java]
//        val userData = userViewModel.userData.value?.data ?: UserData("", "", "", true, true, feeds = [])
//        // UserData 사용
//        val canISeeFeeds = userData.canISeeFeeds
//
//        // 토글 버튼의 상태 업데이트
//        toggleButton.isChecked = canISeeFeeds
//
//        // 토글 버튼 상태에 따라 적절한 동작 수행
//        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
//            // isChecked 값에 따라 canISeeFeeds 값을 업데이트
//            userData.canISeeFeeds = isChecked
//            // 업데이트 된 값으로 서버에 저장하는 API 호출 등의 동작 수행
//        }
//
//        userViewModel.userData.postValue(userData)
        binding.btnLogout.setOnClickListener {
            sharedManager.removeCurrentToken()
            sharedManager.removeCurrentUser()
            sharedManager.removeChatRoomList()
            sharedManager.removeChatList()
            requireActivity().stopService(Intent(requireContext(), ImmortalService::class.java))
            it.findNavController().navigate(R.id.action_settingFragment_to_signupActivity)
        }

        return binding.root
    }
}
