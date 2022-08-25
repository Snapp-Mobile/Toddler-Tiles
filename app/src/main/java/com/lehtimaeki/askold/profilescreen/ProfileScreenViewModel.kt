package com.lehtimaeki.askold.profilescreen

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.MyApplication
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileScreenViewModel : ViewModel() {
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