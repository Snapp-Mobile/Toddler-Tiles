package com.lehtimaeki.askold.landingscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.lehtimaeki.askold.R
import com.lehtimaeki.askold.profilescreen.ProfileScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingScreenActivity : AppCompatActivity() {

    private val viewModel: LandingScreenActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_screen_activity)
        if (savedInstanceState == null) {
            when (val destination = viewModel.getDestination()) {
                is Destination.LandingScreen -> navigateTo(
                    LandingScreenFragment.newInstance(
                        destination.userName
                    )
                )
                is Destination.ProfileScreen -> navigateTo(ProfileScreenFragment.newInstance())
            }
        }
    }

    private fun navigateTo(destination: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, destination)
            .commitNow()
    }
}