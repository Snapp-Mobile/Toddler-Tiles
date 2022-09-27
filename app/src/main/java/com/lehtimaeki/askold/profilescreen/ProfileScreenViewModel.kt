package com.lehtimaeki.askold.profilescreen

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor() : ViewModel() {
    var name = MutableStateFlow("")
    private var _name = String()

    fun setName(babyName: String) {
        _name = babyName
        name.value = _name
    }

    fun saveData() {
        val sharedPref =
            MyApplication.getAppContext()?.getSharedPreferences("name", MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("name", name.value)
            apply()
        }
        sharedPref.getString("name", null).toString()
    }
}