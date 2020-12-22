package com.lehtimaeki.askold

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import java.util.*


object ColorPalettes {

    private val ALL_PALETTES = listOf(
        listOf("#f72585", "#b5179e", "#7209b7", "#560bad", "#480ca8", "#3a0ca3","#3f37c9","#4361ee","#4895ef","#4cc9f0"),
        listOf("#9b5de5", "#f15bb5", "#fee440", "#00bbf9", "#00f5d4"),
        listOf("#390099", "#9e0059", "#ff0054", "#ff5400", "#ffbd00"),
        listOf("#edf67d", "#f896d8", "#ca7df9", "#724cf9", "#564592"),
        listOf("#006ba6", "#0496ff", "#ffbc42", "#d81159", "#8f2d56"),
    )


    var currentPaletteIndex = 0
    var currentColorIndexInPalette = 0

    private val rnd = Random()

    @ColorInt
    fun getNextColorFromPalette(): Int {

        synchronized(this) {
            if (currentColorIndexInPalette >= ALL_PALETTES[currentPaletteIndex].size) {
                // randomise next palette
                currentPaletteIndex = rnd.nextInt(ALL_PALETTES.size)
                currentColorIndexInPalette = 0
            }
            return Color.parseColor(ALL_PALETTES[currentPaletteIndex][currentColorIndexInPalette++])
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