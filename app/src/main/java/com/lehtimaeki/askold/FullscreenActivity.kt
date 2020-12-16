package com.lehtimaeki.askold

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.appcompat.app.AppCompatActivity
import com.lehtimaeki.askold.databinding.ActivityFullscreenBinding

/**
 */
class FullscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullscreenBinding


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideChrome()
    }


    override fun onBackPressed() {
        // not allowed
    }

    private fun hideChrome() {
        binding.root.windowInsetsController?.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        binding.root.windowInsetsController?.hide(
            android.view.WindowInsets.Type.systemBars()
        )
    }
}