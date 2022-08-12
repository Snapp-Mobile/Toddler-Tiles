package com.lehtimaeki.askold

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import java.util.*


object ColorPalettes {
    private val PRIMARY_PALETTE = listOf(
        "#5DDAD0",
        "#FFB37B",
        "#FF9DAF",
        "#FFD467",
        "#D5A0FE",
        "#9CF8B5",
        "#FF8888",
        "#FFCA7A",
        "#C0FFFB",
        "#88E2FF",
        "#FF88D7",
        "#88A9FF",
        "#85F29D",
        "#FFF388",
        "#AA9CFD"
    )


    private val LIGHT_PALETTE = listOf(
        "#DFF5F5",
        "#FDEAE3",
        "#FFE7EC",
        "#FFF8E4",
        "#F5E8FE",
        "#E7FDED",
        "#FFE7EB",
        "#FFF3DF",
        "#ECFFFE",
        "#E3F8FF",
        "#FFE3F5",
        "#E5EAFF",
        "#EAFDEE",
        "#FFFDE9",
        "#ECE7FE",
    )

    var currentColorIndexInPalette = 0

    private val rnd = Random()

    @ColorInt
    fun getNextColorFromPalette(useLightPalette: Boolean?): Int {
        val palette = if (useLightPalette == true) {
            LIGHT_PALETTE
        } else {
            PRIMARY_PALETTE
        }

        synchronized(this) {
            if (currentColorIndexInPalette >= palette.size) {
                currentColorIndexInPalette = 0
            }
            return Color.parseColor(palette[currentColorIndexInPalette++])
        }
    }

    @ColorInt
    fun getInverseColor(@ColorInt color: Int): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val alpha = Color.alpha(color)
        return Color.argb(alpha, 255 - red, 255 - green, 255 - blue)
    }

    @ColorInt
    fun getContrastColor(@ColorInt color: Int): Int {
        val whiteContrast = ColorUtils.calculateContrast(Color.WHITE, color)
        val blackContrast = ColorUtils.calculateContrast(Color.BLACK, color)
        return if (whiteContrast > blackContrast) Color.WHITE else Color.BLACK
    }
}