package com.lehtimaeki.askold

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.ImageViewCompat
import com.lehtimaeki.askold.ColorPalettes.getNextColorFromPalette
import com.lehtimaeki.askold.databinding.GameTileBinding
import com.lehtimaeki.askold.iconset.IconSetRepo
import kotlin.math.absoluteValue

class GameTile @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding = GameTileBinding.inflate(LayoutInflater.from(context), this)

    private var isFlipped = false
    private var bounceFlips = false

    private val minimumDragDistance by lazy { resources.getDimension(R.dimen.minimum_drag_distance) }

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

    private fun flipZoom() {
        bounceFlips = true
        if (isFlipped) {
            animateToFrontZoom()
        } else {
            animateToBackZoom()
        }
    }

    private fun flipHorizontal(reverse: Boolean) {
        bounceFlips = true
        if (isFlipped) {
            animateToFrontHorizontal(reverse)
        } else {
            animateToBackHorizontal(reverse)
        }
    }

    private fun flipVertical(reverse: Boolean) {
        bounceFlips = true
        if (isFlipped) {
            animateToFrontVertical(reverse)
        } else {
            animateToBackVertical(reverse)
        }
    }

    private fun animateToFrontHorizontal(reverse: Boolean) {
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

    private fun animateToFrontVertical(reverse: Boolean) {
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

    private fun animateToFrontZoom() {
        animate().scaleX(0f).scaleY(0f).withEndAction {
            swapToFront()
            animate().scaleX(1f).scaleY(1f).withEndAction { resetBounce() }
        }
    }

    private fun animateToBackHorizontal(reverse: Boolean) {
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

    private fun animateToBackVertical(reverse: Boolean) {
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

    private fun animateToBackZoom() {
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
        binding.back.setCardBackgroundColor(newBackground)
        binding.front.isGone = true
        binding.back.isVisible = true

        handleContent(newBackground,binding.backImage)
    }

    private fun swapToFront() {
        val newBackground = getNextColorFromPalette()
        binding.front.setCardBackgroundColor(newBackground)
        binding.front.isVisible = true
        binding.back.isGone = true

        handleContent(newBackground, binding.frontImage)
    }

    private fun handleContent(backgroundColor: Int, imageView: ImageView) {
        val (icon, tintForContrast) = IconSetRepo.getRandomIcon()
        imageView.setImageResource(icon)
        if(tintForContrast){
            ImageViewCompat.setImageTintList(
                imageView,
                ColorStateList.valueOf(ColorPalettes.getContrastColor(backgroundColor))
            )
        }else{
            ImageViewCompat.setImageTintList(imageView, null)
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
}