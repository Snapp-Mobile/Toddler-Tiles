package com.lehtimaeki.askold.iconset

data class IconSet(
    val id: Int,
    val name: String,
    var isUnlocked: Boolean,
    val icons: List<Int>,
    val tintForContrast: Boolean = false
)