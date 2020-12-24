package com.lehtimaeki.askold

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.lehtimaeki.askold.databinding.FragmentMainBinding


/**
 */
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var allTiles: List<GameTile> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        inflateColumns(binding.columnsContainer)
        setupTouchInterceptor()
        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchInterceptor() {
        binding.touchLayer.setOnTouchListener { _, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val coords = MotionEvent.PointerCoords()

                    for (i in 0 until event.pointerCount) {
                        event.getPointerCoords(i, coords)

                        allTiles.forEach {
                            it.onTouchOnOverlay(coords.x, coords.y, i)
                        }
                    }
                    true
                }
                MotionEvent.ACTION_UP-> {
                    val coords = MotionEvent.PointerCoords()

                    for (i in 0 until event.pointerCount) {
                        event.getPointerCoords(i, coords)

                        allTiles.forEach {
                            it.onTouchUpOnOverlay(coords.x, coords.y, i)
                        }
                    }
                    true
                } else -> {
                    false
                }
            }

        }
    }


    private fun inflateColumns(columnsContainer: ViewGroup) {

        val newTiles = mutableListOf<GameTile>()

        val numberOfColumns =
            (requireActivity().windowManager.currentWindowMetrics.bounds.width() /
                    resources.getDimension(R.dimen.minimal_tile_size)).toInt()
        for (i in 0 until numberOfColumns) {
            val column = layoutInflater.inflate(R.layout.one_column, columnsContainer, false)
            inflateRows(column as ViewGroup)

            column.forEach {
                (it as? GameTile)?.let {
                    newTiles.add(it)
                }
            }

            columnsContainer.addView(column)
        }

        allTiles = newTiles
    }

    private fun inflateRows(column: ViewGroup) {
        val numberOfRows =
            (requireActivity().windowManager.currentWindowMetrics.bounds.height() /
                    resources.getDimension(R.dimen.minimal_tile_size)).toInt()
        for (i in 0 until numberOfRows) {
            layoutInflater.inflate(R.layout.one_tile, column)
        }
    }
}