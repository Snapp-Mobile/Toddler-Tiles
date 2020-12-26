package com.lehtimaeki.askold

import android.content.Context
import android.content.res.ColorStateList
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
import kotlin.math.absoluteValue


class GameTile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val rnd = Random()
    private var isFlipped = false


    private val minimumDragDistance by lazy { resources.getDimension(R.dimen.minimum_drag_distance) }

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
    }


    private var bounceFlips = false


    private fun flipZoom() {
        bounceFlips = true
        if (isFlipped) {
            animateToFront3()
        } else {
            animateToBack3()
        }
    }


    private fun flipHorizontal(reverse: Boolean) {
        bounceFlips = true
        if (isFlipped) {
            animateToFront1(reverse)
        } else {
            animateToBack1(reverse)
        }
    }


    private fun flipVertical(reverse: Boolean) {
        bounceFlips = true
        if (isFlipped) {
            animateToFront2(reverse)
        } else {
            animateToBack2(reverse)
        }
    }


    private fun animateToFront1(reverse: Boolean) {
        animate().rotationY(
            if (reverse) {
                -90f
            } else {
                90f
            }
        ).withEndAction {
            swapToFront()
            rotationY = if (reverse) {
                90f
            } else {
                -90f
            }
            animate().rotationY(0f).withEndAction { resetBounce() }
        }
    }

    private fun animateToFront2(reverse: Boolean) {


        animate().rotationX(
            if (reverse) {
                -90f
            } else {
                90f
            }
        ).withEndAction {
            swapToFront()
            rotationX = if (reverse) {
                90f
            } else {
                -90f
            }
            animate().rotationX(0f).withEndAction { resetBounce() }
        }
    }

    private fun animateToFront3() {
        animate().scaleX(0f).scaleY(0f).withEndAction {
            swapToFront()
            animate().scaleX(1f).scaleY(1f).withEndAction { resetBounce() }
        }
    }


    private fun animateToBack1(reverse: Boolean) {
        animate().rotationY(
            if (reverse) {
                -90f
            } else {
                90f
            }
        ).withEndAction {
            swapToBack()
            rotationY = if (reverse) {
                90f
            } else {
                -90f
            }
            animate().rotationY(0f).withEndAction { resetBounce() }
        }
    }

    private fun animateToBack2(reverse: Boolean) {
        animate().rotationX(
            if (reverse) {
                -90f
            } else {
                90f
            }
        ).withEndAction {
            swapToBack()
            rotationX = if (reverse) {
                90f
            } else {
                -90f
            }
            animate().rotationX(0f).withEndAction { resetBounce() }
        }
    }

    private fun animateToBack3() {
        animate().scaleX(0f).scaleY(0f).withEndAction {
            swapToBack()
            animate().scaleX(1f).scaleY(1f).withEndAction { resetBounce() }
        }
    }


    private fun resetBounce() {
        lastTrackedIndexes.clear()
        bounceFlips = false
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
            random < IMAGE_ICON_PROBABILITY -> {
                imageView.isVisible = true
                textView.isGone = true
                imageView.setImageResource(IMAGE_ICONS[rnd.nextInt(IMAGE_ICONS.size)])
                ImageViewCompat.setImageTintList(imageView, null)
            }
            random < SYMBOL_PROBABILITY -> {
                textView.setTextColor(ColorPalettes.getContrastColor(backgroundColor))
                imageView.isGone = true
                textView.isVisible = true
                textView.text = SYMBOLS[rnd.nextInt(SYMBOLS.size)]
            }
            random < ICON_PROBABILITY -> {
                ImageViewCompat.setImageTintList(
                    imageView,
                    ColorStateList.valueOf(ColorPalettes.getContrastColor(backgroundColor))
                )
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


    private val lastTrackedIndexes = mutableMapOf<Int, Pair<Float, Float>>()

    /**
     * This function will trigger
     */
    private fun trackTouchOnThisTile(x: Float, y: Float, pointerIndex: Int) {

        val lastPoint = lastTrackedIndexes[pointerIndex]

        if (lastPoint == null) {
            lastTrackedIndexes[pointerIndex] = Pair(x, y)
            return
        }


        val xAbs = (lastPoint.first - x).absoluteValue
        val yAbs = (lastPoint.second - y).absoluteValue

        if (xAbs > minimumDragDistance && xAbs > yAbs) {
            flipHorizontal(x < lastPoint.first)
        } else if (yAbs > minimumDragDistance) {
            flipVertical(lastPoint.second < y)
        }


    }

    private fun isThisTile(eventX: Float, eventY: Float): Boolean {
        return eventX > x && eventX < x + width && eventY > y && eventY < y + height
    }

    fun onTouchOnOverlay(eventX: Float, eventY: Float, pointerIndex: Int) {
        if (bounceFlips) {
            return
        }
        if (isThisTile(eventX, eventY)) {
            trackTouchOnThisTile(eventX, eventY, pointerIndex)
        } else {
            lastTrackedIndexes.remove(pointerIndex)
        }
    }


    fun onTouchUpOnOverlay(eventX: Float, eventY: Float, pointerIndex: Int) {
        if (bounceFlips) {
            return
        }
        if (isThisTile(eventX, eventY)) {
            flipZoom()
        } else {
            lastTrackedIndexes.remove(pointerIndex)
        }
    }



    companion object {
        const val IMAGE_ICON_PROBABILITY = 70
        const val SYMBOL_PROBABILITY = 80
        const val ICON_PROBABILITY = 90

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

        val IMAGE_ICONS =
            arrayListOf(
                R.drawable.flaticon_01_mouse,
                R.drawable.flaticon_02_cow,
                R.drawable.flaticon_03_kangaroo,
                R.drawable.flaticon_04_bear,
                R.drawable.flaticon_05_flamingo,
                R.drawable.flaticon_06_fox,
                R.drawable.flaticon_07_bat,
                R.drawable.flaticon_08_crab,
                R.drawable.flaticon_09_lion,
                R.drawable.flaticon_10_frog,
                R.drawable.flaticon_11_bee,
                R.drawable.flaticon_12_koala,
                R.drawable.flaticon_13_tiger,
                R.drawable.flaticon_14_rhino,
                R.drawable.flaticon_15_squirrel,
                R.drawable.flaticon_16_whale,
                R.drawable.flaticon_17_duck,
                R.drawable.flaticon_18_camel,
                R.drawable.flaticon_19_shark,
                R.drawable.flaticon_20_bird,
                R.drawable.flaticon_21_rabbit,
                R.drawable.flaticon_22_llama,
                R.drawable.flaticon_23_cat,
                R.drawable.flaticon_24_hedgehog,
                R.drawable.flaticon_25_octopus,
                R.drawable.flaticon_26_snail,
                R.drawable.flaticon_27_giraffe,
                R.drawable.flaticon_28_manta_ray,
                R.drawable.flaticon_29_wolf,
                R.drawable.flaticon_30_penguin,
                R.drawable.flaticon_31_panther,
                R.drawable.flaticon_32_elephant,
                R.drawable.flaticon_33_reindeer,
                R.drawable.flaticon_34_chameleon,
                R.drawable.flaticon_35_crocodile,
                R.drawable.flaticon_36_butterfly,
                R.drawable.flaticon_37_owl,
                R.drawable.flaticon_38_turtle,
                R.drawable.flaticon_39_snake,
                R.drawable.flaticon_40_polar_bear,
                R.drawable.flaticon_41_monkey,
                R.drawable.flaticon_42_chicken,
                R.drawable.flaticon_43_sloth,
                R.drawable.flaticon_44_dog,
                R.drawable.flaticon_45_dolphin,
                R.drawable.flaticon_46_pig,
                R.drawable.flaticon_47_hippopotamus,
                R.drawable.flaticon_48_parrot,
                R.drawable.flaticon_49_clownfish,
                R.drawable.flaticon_50_horse,

                R.drawable.flaticon_seelife_01_sea_anemone,
                R.drawable.flaticon_seelife_02_seaweed,
                R.drawable.flaticon_seelife_03_puffer_fish,
                R.drawable.flaticon_seelife_04_sardine,
                R.drawable.flaticon_seelife_05_sea_urchin,
                R.drawable.flaticon_seelife_06_codfish,
                R.drawable.flaticon_seelife_07_surgeon_fish,
                R.drawable.flaticon_seelife_08_eel,
                R.drawable.flaticon_seelife_09_moorish_idol,
                R.drawable.flaticon_seelife_10_bubbles,
                R.drawable.flaticon_seelife_11_seaweed,
                R.drawable.flaticon_seelife_12_sunset,
                R.drawable.flaticon_seelife_13_tuna,
                R.drawable.flaticon_seelife_14_jellyfish,
                R.drawable.flaticon_seelife_15_hammerhead_fish,
                R.drawable.flaticon_seelife_16_seagull,
                R.drawable.flaticon_seelife_17_sea_snake,
                R.drawable.flaticon_seelife_18_penguin,
                R.drawable.flaticon_seelife_19_orca,
                R.drawable.flaticon_seelife_20_shrimp,
                R.drawable.flaticon_seelife_21_swordfish,
                R.drawable.flaticon_seelife_22_ray,
                R.drawable.flaticon_seelife_23_fish,
                R.drawable.flaticon_seelife_24_crab,
                R.drawable.flaticon_seelife_25_octopus,
                R.drawable.flaticon_seelife_26_hermit_crab,
                R.drawable.flaticon_seelife_27_shoal,
                R.drawable.flaticon_seelife_28_seahorse,
                R.drawable.flaticon_seelife_29_clown_fish,
                R.drawable.flaticon_seelife_30_seashell,
                R.drawable.flaticon_seelife_31_shark,
                R.drawable.flaticon_seelife_32_seashell,
                R.drawable.flaticon_seelife_33_whale,
                R.drawable.flaticon_seelife_34_jellyfish,
                R.drawable.flaticon_seelife_35_flying_fish,
                R.drawable.flaticon_seelife_36_anglerfish,
                R.drawable.flaticon_seelife_37_starfish,
                R.drawable.flaticon_seelife_38_dolphin,
                R.drawable.flaticon_seelife_39_angelfish,
                R.drawable.flaticon_seelife_40_lobster,
                R.drawable.flaticon_seelife_41_squid,
                R.drawable.flaticon_seelife_42_sunfish,
                R.drawable.flaticon_seelife_43_lifesaver,
                R.drawable.flaticon_seelife_44_coral,
                R.drawable.flaticon_seelife_45_shell,
                R.drawable.flaticon_seelife_46_seal,
                R.drawable.flaticon_seelife_47_ship,
                R.drawable.flaticon_seelife_48_fishbone,
                R.drawable.flaticon_seelife_49_mussel,
                R.drawable.flaticon_seelife_50_turtle,


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
}