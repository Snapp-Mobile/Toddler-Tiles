package com.lehtimaeki.askold.landingscreen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.MyApplication

class LandingScreenActivityViewModel : ViewModel() {

    private fun getName(): String? {
        val sharedPref =
            MyApplication.getAppContext()?.getSharedPreferences("name", Context.MODE_PRIVATE)
        return sharedPref?.getString("name", null)
    }

    fun getDestination(): Destination {
        val name = getName()
        return if (name == null) {
            Destination.ProfileScreen
        } else {
            Destination.LandingScreen(name)
        }
    }
}

sealed class Destination {
    object ProfileScreen : Destination()
    class LandingScreen(val name: String) : Destination()
}



