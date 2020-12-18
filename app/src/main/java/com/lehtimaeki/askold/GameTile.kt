package com.lehtimaeki.askold

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import com.google.android.material.card.MaterialCardView
import com.lehtimaeki.askold.ColorPalettes.getNextColorFromPalette
import java.util.*


class GameTile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val rnd = Random()
    private var isFlipped = false

    private val frontSide by lazy { findViewById<MaterialCardView>(R.id.front) }
    private val backSide by lazy { findViewById<MaterialCardView>(R.id.back) }

    private val frontText by lazy { findViewById<TextView>(R.id.frontText) }
    private val backText by lazy { findViewById<TextView>(R.id.backText) }


    private val frontImage by lazy { findViewById<ImageView>(R.id.frontImage) }
    private val backImage by lazy { findViewById<ImageView>(R.id.backImage) }


    private val x by lazy {
        val location = IntArray(2)
        getLocationOnScreen(location)
        location[0]
    }

    private val y by lazy {
        val location = IntArray(2)
        getLocationOnScreen(location)
        location[1]
    }


    init {
        inflate(getContext(), R.layout.game_tile, this)
//        setOnClickListener {
//            flip()
//        }
//        setOnTouchListener { v, event ->
//
//            if (event.action == MotionEvent.ACTION_DOWN) {
//                flip()
//                true
//            } else {
//                false
//            }
//
//
//        }
    }


    var bounceFlips = false

    fun flip() {
        if(bounceFlips){
            return
        }
        bounceFlips = true

        if (isFlipped) {
            when (rnd.nextInt(3)) {
                0 -> animateToFront1()
                1 -> animateToFront2()
                2 -> animateToFront3()
            }

        } else {
            when (rnd.nextInt(3)) {
                0 -> animateToBack1()
                1 -> animateToBack2()
                2 -> animateToBack3()
            }

        }
        isFlipped = !isFlipped
    }


    private fun animateToFront1() {
        animate().rotationY(90f).withEndAction {
            swapToFront()
            rotationY = -90f
            animate().rotationY(0f).withEndAction { bounceFlips = false }
        }
    }

    private fun animateToFront2() {
        animate().rotationX(90f).withEndAction {
            swapToFront()
            rotationX = -90f
            animate().rotationX(0f).withEndAction { bounceFlips = false }
        }
    }

    private fun animateToFront3() {
        animate().scaleX(0f).scaleY(0f).withEndAction {
            swapToFront()
            animate().scaleX(1f).scaleY(1f).withEndAction { bounceFlips = false }
        }
    }


    private fun animateToBack1() {
        animate().rotationY(90f).withEndAction {
            swapToBack()
            rotationY = -90f
            animate().rotationY(0f).withEndAction { bounceFlips = false }
        }
    }

    private fun animateToBack2() {
        animate().rotationX(90f).withEndAction {
            swapToBack()
            rotationX = -90f
            animate().rotationX(0f).withEndAction { bounceFlips = false }
        }
    }

    private fun animateToBack3() {
        animate().scaleX(0f).scaleY(0f).withEndAction {
            swapToBack()
            animate().scaleX(1f).scaleY(1f).withEndAction { bounceFlips = false }
        }
    }


    private fun swapToBack() {
        val newBackground = getNextColorFromPalette()
        backSide.setCardBackgroundColor(newBackground)
        frontSide.isGone = true
        backSide.isVisible = true

        handleContent(newBackground, backText, backImage)
    }


    private fun swapToFront() {
        val newBackground = getNextColorFromPalette()
        frontSide.setCardBackgroundColor(newBackground)
        frontSide.isVisible = true
        backSide.isGone = true

        handleContent(newBackground, frontText, frontImage)
    }


    private fun handleContent(backgroundColor: Int, textView: TextView, imageView: ImageView) {


        val random = rnd.nextInt(100)
        when {
            random < SYMBOL_PROBABILITY -> {
                textView.setTextColor(getInverseColor(backgroundColor))
                imageView.isGone = true
                textView.isVisible = true
                textView.text = SYMBOLS[rnd.nextInt(SYMBOLS.size)]
            }
            random < ICON_PROBABILITY -> {
                ImageViewCompat.setImageTintList(
                    imageView,
                    ColorStateList.valueOf(getInverseColor(backgroundColor))
                );
                imageView.isVisible = true
                textView.isGone = true
                imageView.setImageResource(ICONS[rnd.nextInt(ICONS.size)])
            }
            else -> {
                textView.isGone = true
                imageView.isGone = true
            }
        }


    }


    companion object {

        const val SYMBOL_PROBABILITY = 40
        const val ICON_PROBABILITY = 80

        val ICONS =
            arrayListOf(
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_baseline_ac_unit_24,
                R.drawable.ic_baseline_airport_shuttle_24,
                R.drawable.ic_baseline_brightness_3_24,
                R.drawable.ic_baseline_brightness_5_24,
                R.drawable.ic_baseline_child_friendly_24,
                R.drawable.ic_baseline_cloud_24,
                R.drawable.ic_baseline_directions_bike_24,
                R.drawable.ic_baseline_directions_boat_24,
                R.drawable.ic_baseline_emoji_emotions_24,
                R.drawable.ic_baseline_local_shipping_24,
            )

        val SYMBOLS =
            arrayListOf(
                "A",
                "B",
                "C",
                "D",
                "E",
                "F",
                "G",
                "H",
                "I",
                "J",
                "K",
                "L",
                "M",
                "N",
                "O",
                "P",
                "Q",
                "R",
                "S",
                "T",
                "U",
                "V",
                "W",
                "X",
                "Y",
                "Y",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10"
            )

    }

    private fun getInverseColor(color: Int): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val alpha = Color.alpha(color)
        return Color.argb(alpha, 255 - red, 255 - green, 255 - blue)
    }

    fun isThisTileInCoordinate(eventX: Float, eventY: Float): Boolean {
        return eventX > x && eventX < x + width && eventY > y && eventY < y + height
    }
}