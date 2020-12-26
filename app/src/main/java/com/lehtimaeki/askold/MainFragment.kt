package com.lehtimaeki.askold

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.lehtimaeki.askold.databinding.FragmentMainBinding
import com.lehtimaeki.askold.delegates.viewBinding

class MainFragment : Fragment(R.layout.fragment_main) {
    private val binding by viewBinding(FragmentMainBinding::bind)
    private val allTiles = mutableListOf<GameTile>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inflateColumns(binding.columnsContainer)
        setupTouchInterceptor()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchInterceptor() {
        binding.touchLayer.setOnTouchListener { _, event ->
            val coordinates = MotionEvent.PointerCoords()

            return@setOnTouchListener when (event.action) {
                MotionEvent.ACTION_DOWN -> true
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                    for (i in 0 until event.pointerCount) {
                        event.getPointerCoords(i, coordinates)

                        allTiles.forEach { gameTile ->
                            when (event.action) {
                                MotionEvent.ACTION_MOVE ->
                                    gameTile.onTouchOnOverlay(coordinates.x, coordinates.y, i)
                                MotionEvent.ACTION_UP ->
                                    gameTile.onTouchUpOnOverlay(coordinates.x, coordinates.y, i)
                            }
                        }
                    }

                    true
                }

                else -> false
            }
        }
    }

    private fun inflateColumns(columnsContainer: ViewGroup) {
        val newTiles = mutableListOf<GameTile>()

        val metrics = resources.displayMetrics
        val height = metrics.heightPixels
        val width = metrics.widthPixels

        val numberOfColumns = (width / resources.getDimension(R.dimen.minimal_tile_size)).toInt()

        for (i in 0 until numberOfColumns) {
            val column = layoutInflater.inflate(R.layout.one_column, columnsContainer, false)
            inflateRows(column as ViewGroup, height)

            column.forEach { gameTileView ->
                if (gameTileView is GameTile) {
                    newTiles.add(gameTileView)
                }
            }

            columnsContainer.addView(column)
        }

        with(allTiles) {
            clear()
            addAll(newTiles)
        }
    }

    private fun inflateRows(column: ViewGroup, height: Int) {
        val numberOfRows = (height / resources.getDimension(R.dimen.minimal_tile_size)).toInt()
        for (i in 0 until numberOfRows) {
            layoutInflater.inflate(R.layout.one_tile, column)
        }
    }
}