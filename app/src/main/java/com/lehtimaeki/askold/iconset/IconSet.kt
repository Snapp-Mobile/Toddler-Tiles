package com.lehtimaeki.askold.iconset

import com.lehtimaeki.askold.R
import java.io.Serializable

data class IconSet(
    val id: Int,
    val name: String,
    var isUnlocked: Boolean,
    val icons: List<Int>,
    val tintForContrast: Boolean = false,
    val useLightPalette: Boolean = false,
    val excludeFirstAsset: Boolean = false,
    var itemTypeStringResourceId: Int = R.string.free
) : Serializable