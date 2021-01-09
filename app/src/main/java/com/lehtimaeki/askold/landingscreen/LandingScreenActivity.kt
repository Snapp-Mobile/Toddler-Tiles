package com.lehtimaeki.askold.landingscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lehtimaeki.askold.R

class LandingScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_screen_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LandingScreenFragment.newInstance())
                .commitNow()
        }
    }
}