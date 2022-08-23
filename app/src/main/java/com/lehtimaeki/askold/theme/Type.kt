package com.lehtimaeki.askold.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lehtimaeki.askold.R

// Set of Material typography styles to start with

val Cookie = FontFamily(Font(R.font.cookie_regular))
val Roboto = FontFamily(Font(R.font.roboto_regular))
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Cookie,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    h2 = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)