package com.lehtimaeki.askold.profilescreen

import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.data.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(private val sharedPref: UserPreferences) :
    ViewModel() {
    var name = MutableStateFlow("")
    private var _name = String()

    fun setName(babyName: String) {
        _name = babyName
        name.value = _name
    }

    fun getData() {
        sharedPref.saveUserName(name.value)
        sharedPref.getUserName()
    }
}