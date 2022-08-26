package com.lehtimaeki.askold.landingscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.lehtimaeki.askold.R
import com.lehtimaeki.askold.profilescreen.ProfileScreenFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LandingScreenActivity : AppCompatActivity() {

    private val viewModel: LandingScreenActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_screen_activity)
        if (savedInstanceState == null) {
            viewModel.getData()
            viewModel.name.onEach {
                if (it == "") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProfileScreenFragment.newInstance())
                        .commitNow()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, LandingScreenFragment.newInstance(it))
                        .commitNow()
                }
            }.launchIn(lifecycleScope)
        }
    }
}