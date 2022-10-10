package com.lehtimaeki.askold.landingscreen

import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.data.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LandingScreenActivityViewModel @Inject constructor(private val sharedPref: UserPreferences) :
    ViewModel() {

    private fun getUserName(): String? {
        return sharedPref.getUserName()
    }

    fun getDestination(): Destination {
        val userName = getUserName()
        return if (userName == null) {
            Destination.ProfileScreen
        } else {
            Destination.LandingScreen(userName)
        }
    }
}

sealed class Destination {
    object ProfileScreen : Destination()
    class LandingScreen(val userName: String) : Destination()
}



