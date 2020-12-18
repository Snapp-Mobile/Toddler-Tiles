package com.lehtimaeki.askold

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lehtimaeki.askold.databinding.FragmentMainBinding


/**
 */
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        inflateColumns(binding.columnsContainer)
        setupTouchInterceptor()
        return binding.root
    }


    // TODO: see if this is needed
    private fun setupTouchInterceptor(){
        binding.touchLayer.setOnTouchListener { v, event ->
            false
        }
    }


    private fun inflateColumns(columnsContainer: ViewGroup) {
        val numberOfColumns =
            (requireActivity().windowManager.currentWindowMetrics.bounds.width() /
                    resources.getDimension(R.dimen.minimal_tile_size)).toInt()
        for (i in 0 until numberOfColumns) {
            val column = layoutInflater.inflate(R.layout.one_column, columnsContainer, false)
            inflateRows(column as ViewGroup)

            columnsContainer.addView(column)
        }
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