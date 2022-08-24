package com.lehtimaeki.askold.profilescreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileScreenViewModel : ViewModel() {
    var name = MutableStateFlow("")
    private var _name = String()

    fun setName(babyName: String) {
        _name = babyName
        name.value = _name
    }
}