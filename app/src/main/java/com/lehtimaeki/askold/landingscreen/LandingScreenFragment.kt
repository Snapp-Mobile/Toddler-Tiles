package com.lehtimaeki.askold.landingscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lehtimaeki.askold.FullscreenActivity
import com.lehtimaeki.askold.FullscreenActivity.Companion.ICON_SET_EXTRA_ID
import com.lehtimaeki.askold.R
import com.lehtimaeki.askold.databinding.LandingScreenFragmentBinding
import com.lehtimaeki.askold.delegates.viewBinding
import com.lehtimaeki.askold.iconset.IconSet

class LandingScreenFragment : Fragment(R.layout.landing_screen_fragment) {

    private val binding by viewBinding(LandingScreenFragmentBinding::bind)

    companion object {
        fun newInstance() = LandingScreenFragment()
    }

    private val viewModel: LandingScreenViewModel by viewModels()
    private val adapter = IconSetAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val metrics = resources.displayMetrics
        val width = metrics.widthPixels

        val numberOfColumns = (width / resources.getDimension(R.dimen.minimal_tile_size)).toInt()

        val layoutManager = GridLayoutManager(context, numberOfColumns)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.dataSet.get(position).iconSet != null) {
                    1
                } else {
                    numberOfColumns
                }
            }
        }

        binding.recyclerview.layoutManager = layoutManager
        binding.recyclerview.adapter = adapter

        viewModel.iconSetsLiveData.observe(viewLifecycleOwner, {
            val data = mutableListOf<IconSetWrapper>()
            data.add(IconSetWrapper(0, null, "Begin Learning"))
            it.first.forEach { iconSet ->
                data.add(IconSetWrapper(iconSet.id, iconSet, null))
            }

            if (it.second.isNotEmpty()) {
                data.add(IconSetWrapper(Int.MAX_VALUE, null, "More fun sets"))
                it.second.forEach { iconSet ->
                    data.add(IconSetWrapper(iconSet.id, iconSet, null))
                }
            }
            adapter.dataSet = data
        })
    }


    inner class IconSetAdapter :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        val VIEW_TYPE_ICON_SET = 0
        val VIEW_TYPE_LABEL = 1


        var dataSet: List<IconSetWrapper> = listOf()
            set(value) {
                field = value
                notifyDataSetChanged()
            }


        override fun getItemViewType(position: Int): Int {
            return when {
                dataSet[position].iconSet != null -> VIEW_TYPE_ICON_SET
                else -> VIEW_TYPE_LABEL
            }
        }


        inner class IconSetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.label)
            val iconView: ImageView = view.findViewById(R.id.image)
        }

        inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.label)
        }

        override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            viewType: Int
        ): RecyclerView.ViewHolder {


            return when (viewType) {
                VIEW_TYPE_ICON_SET -> IconSetViewHolder(
                    LayoutInflater.from(viewGroup.context)
                        .inflate(R.layout.landing_screen_tile, viewGroup, false)
                )
                else -> TitleViewHolder(
                    LayoutInflater.from(viewGroup.context)
                        .inflate(R.layout.landing_screen_group_title, viewGroup, false)
                )
            }
        }


        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            when (viewHolder) {
                is IconSetViewHolder -> onBindViewHolder(viewHolder, position)
                is TitleViewHolder -> onBindViewHolder(viewHolder, position)
            }
        }


        private fun onBindViewHolder(viewHolder: IconSetViewHolder, position: Int) {
            viewHolder.textView.text = dataSet[position].iconSet?.name
            viewHolder.iconView.setImageResource(
                dataSet[position].iconSet?.icons?.first()
                    ?: throw RuntimeException("ended up in wrong viewholder bind")
            )

            viewHolder.itemView.setOnClickListener {
                activity?.startActivity(Intent(context, FullscreenActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putInt(ICON_SET_EXTRA_ID, dataSet[position].id)
                    })
                })
            }
        }


        private fun onBindViewHolder(viewHolder: TitleViewHolder, position: Int) {
            viewHolder.textView.text = dataSet[position].label
        }

        override fun getItemCount() = dataSet.size
    }
}


data class IconSetWrapper(val id: Int, val iconSet: IconSet?, val label: String?)