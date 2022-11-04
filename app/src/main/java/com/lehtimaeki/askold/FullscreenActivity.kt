package com.lehtimaeki.askold

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.lehtimaeki.askold.databinding.ActivityFullscreenBinding
import com.lehtimaeki.askold.delegates.viewBinding

class FullscreenActivity : AppCompatActivity() {

    companion object {
        const val ICON_SET_EXTRA_ID = "iconset"
        const val IS_LARGE_CARD_MODE_ID = "isLargeCardMode"
    }

    private val binding by viewBinding(ActivityFullscreenBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            MainFragment.newInstance(
                intent.extras?.getInt(ICON_SET_EXTRA_ID) ?: 0,
                intent.extras?.getBoolean(IS_LARGE_CARD_MODE_ID) ?: false
            )
        ).commit()
    }

    override fun onResume() {
        super.onResume()
        hideSystemBars()
    }

//    override fun onBackPressed() {
//        // back prevented so the kid doesn't hit it by accident
//    }

    private fun hideSystemBars() {

        /**
         * IssueTracker link: https://issuetracker.google.com/issues/173203649
         * Fix inspiration: https://stackoverflow.com/a/64828067
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() and WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)
//                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }
}