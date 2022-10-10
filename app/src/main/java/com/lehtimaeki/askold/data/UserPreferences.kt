package com.lehtimaeki.askold.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val sharedPref: SharedPreferences) {

    fun saveUserName(userName: String) {
        with(sharedPref.edit()) {
            putString(KEY_USER_NAME, userName)
            apply()
        }
    }

    fun getUserName(): String? {
        return sharedPref.getString(KEY_USER_NAME, null)
    }

    companion object {
        private const val KEY_USER_NAME = "name"
        const val ASKOLD_SHARED_PREFS = "askold_shared_prefs"
    }
}