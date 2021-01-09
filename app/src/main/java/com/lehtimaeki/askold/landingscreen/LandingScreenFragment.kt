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
import com.lehtimaeki.askold.iconset.IconSetRepo

class LandingScreenFragment : Fragment(R.layout.landing_screen_fragment) {

    private val binding by viewBinding(LandingScreenFragmentBinding::bind)

    companion object {
        fun newInstance() = LandingScreenFragment()
    }

    private val viewModel: LandingScreenViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerview.adapter = IconSetAdapter(IconSetRepo.allIconSets)
    }


    inner class IconSetAdapter(private val dataSet: List<IconSet>) :
        RecyclerView.Adapter<IconSetAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.label)
            val iconView: ImageView = view.findViewById(R.id.image)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.landing_screen_tile, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            viewHolder.textView.text = dataSet[position].name
            viewHolder.iconView.setImageResource(dataSet[position].icons.first())

            viewHolder.itemView.setOnClickListener {
                activity?.startActivity(Intent(context, FullscreenActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putInt(ICON_SET_EXTRA_ID, dataSet[position].id)
                    })
                })
            }
        }

        override fun getItemCount() = dataSet.size

    }
}