package com.b305.vuddy.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b305.vuddy.model.FriendProfile

class SharedViewModel : ViewModel() {
    private val _selectedProfile = MutableLiveData<FriendProfile>()
    val selectedProfile: MutableLiveData<FriendProfile> get() = _selectedProfile

    fun selectProfile(profile: FriendProfile) {
        _selectedProfile.value = profile
    }
}
