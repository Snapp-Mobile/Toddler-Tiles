package com.lehtimaeki.askold

import android.app.Application
import android.content.Context
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
        context = this
    }

    companion object {
        private var context: MyApplication? = null
        fun getAppContext(): Context? {
            return context
        }
    }
}