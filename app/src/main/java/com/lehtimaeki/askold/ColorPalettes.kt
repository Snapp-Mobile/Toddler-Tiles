package com.lehtimaeki.askold

import android.graphics.Color
import java.util.*

object ColorPalettes {

    val ALL_PALETTES = listOf(
        listOf("#264653", "#2a9d8f", "#e9c46a", "#f4a261", "#e76f51"),
        listOf("#cb997e", "#eddcd2", "#fff1e6", "#f0efeb", "#ddbea9", "#a5a58d", "#b7b7a4"),
        listOf("#00296b", "#003f88", "#00509d", "#fdc500", "#ddbea9", "#a5a58d", "#ffd500"),
        listOf("#ffbe0b", "#fb5607", "#ff006e", "#8338ec", "#3a86ff"),
        listOf("#f72585", "#7209b7", "#3a0ca3", "#4361ee", "#4cc9f0"),
        listOf("#ff595e", "#ffca3a", "#8ac926", "#1982c4", "#6a4c93"),
        listOf("#d3f8e2", "#e4c1f9", "#f694c1", "#ede7b1", "#a9def9"),
    )


    var currentPaletteIndex = 0
    var currentColorIndexInPalette = 0

    val rnd = Random()

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

}