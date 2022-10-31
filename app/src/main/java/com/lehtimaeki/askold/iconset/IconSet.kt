package com.lehtimaeki.askold.iconset

import java.io.Serializable

data class IconSet(
    val id: Int,
    val name: String,
    var isUnlocked: Boolean,
    val icons: List<Int>,
    val tintForContrast: Boolean = false,
    val useLightPalette: Boolean = false,
    val excludeFirstAsset: Boolean = false
) : Serializable