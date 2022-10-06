package com.lehtimaeki.askold.data

import android.content.Context
import android.content.SharedPreferences
import com.lehtimaeki.askold.MyApplication
import javax.inject.Inject

class UserPreferences @Inject constructor() {
    private val sharedPref: SharedPreferences? =
        MyApplication.getAppContext()
            ?.getSharedPreferences(ASKOLD_SHARED_PREFS, Context.MODE_PRIVATE)

    fun saveUserName(userName: String) {
        sharedPref ?: return
        with(sharedPref.edit()) {
            putString(KEY_USER_NAME, userName)
            apply()
        }
    }

    fun getUserName(): String? {
        return sharedPref?.getString(KEY_USER_NAME, null)
    }

    companion object {
        private const val KEY_USER_NAME = "name"
        private const val ASKOLD_SHARED_PREFS = "askold_shared_prefs"
    }
}