package com.lehtimaeki.askold.landingscreen

import android.content.Context
import android.util.AttributeSet
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AbstractComposeView

class LandingCompose @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractComposeView(context, attrs, defStyle) {

    var text by mutableStateOf("")

    @Composable
    override fun Content() {
        SetLabelText(text)
    }
}

@Composable
fun SetLabelText(
    text: String
) {
    Text(text, color = Color.White)
}