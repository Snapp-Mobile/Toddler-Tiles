package com.lehtimaeki.askold.profilescreen

import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.data.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val sharedPref: UserPreferences) :
    ViewModel() {

    var userName = MutableStateFlow("")
    private var _userName = String()

    fun setUserName(userName: String) {
        _userName = userName
        this.userName.value = _userName
    }

    fun getUserName() {
        sharedPref.saveUserName(userName.value)
        sharedPref.getUserName()
    }
}