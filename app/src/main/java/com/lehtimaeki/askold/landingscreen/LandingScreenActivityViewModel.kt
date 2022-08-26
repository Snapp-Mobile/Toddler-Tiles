package com.lehtimaeki.askold.landingscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.MyApplication
import kotlinx.coroutines.flow.MutableStateFlow

class LandingScreenActivityViewModel : ViewModel() {

    var name = MutableStateFlow("")

    fun getData() {
        val sharedPref =
            MyApplication.getAppContext()?.getSharedPreferences("name", Context.MODE_PRIVATE)
        name.value = sharedPref?.getString("name", null).toString()
    }
}