package com.lehtimaeki.askold

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.WindowInsetsControllerCompat
import com.lehtimaeki.askold.databinding.ActivityFullscreenBinding
import com.lehtimaeki.askold.delegates.viewBinding

class FullscreenActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityFullscreenBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        hideChrome()
    }

    override fun onBackPressed() {
        // back prevented so the kid doesn't hit it by accident
    }

    private fun hideChrome() {
        val insetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
        insetsControllerCompat.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsControllerCompat.hide(systemBars())
    }
}