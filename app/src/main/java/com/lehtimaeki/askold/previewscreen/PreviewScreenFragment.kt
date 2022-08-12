package com.lehtimaeki.askold.previewscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lehtimaeki.askold.ColorPalettes

class PreviewScreenFragment : Fragment() {

    companion object {
        private const val ICON_SET_EXTRA_ID = "iconset"
        private const val ICON_SET_LIGHT_PALLETE = "iconsetLightPallete"

        fun newInstance(iconSetId: Int?, iconSetLightPallete: Boolean?): PreviewScreenFragment {
            val ret = PreviewScreenFragment()

            ret.arguments = Bundle().apply {
                iconSetId?.let {  putInt(ICON_SET_EXTRA_ID, it) }
                iconSetLightPallete?.let {  putBoolean(ICON_SET_LIGHT_PALLETE, it) }
            }

            return ret
        }
    }

    private val viewModel: PreviewScreenViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.loadIconSet(arguments?.getInt(ICON_SET_EXTRA_ID))
        return ComposeView(requireContext()).apply {
            setContent {
                val icons by viewModel.icons.collectAsState()
                IconsList(icons)
            }
        }
    }

    @Composable
    fun ItemImage(
        imageId: Int?, modifier: Modifier
    ) {
        imageId?.let {
            Image(
                painterResource(it), contentDescription = "", modifier = modifier
            )
        }
    }

    @Composable
    fun IconsList(
        icons: List<Int>
    ) {
        Box(
            modifier = Modifier.background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.Center)
                    .background(Color.White),
                columns = GridCells.Adaptive(140.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(start = 32.dp, end = 32.dp)
            ) {
                items(icons) { icons -> Item(icons) }
            }
        }
    }

    @Composable
    fun Item(icons: Int) {
        val color =
            ColorPalettes.getNextColorFromPalette(arguments?.getBoolean(ICON_SET_LIGHT_PALLETE))

        Column {
            Card(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth(),
//                        .clickable(onClick = { })
                elevation = 8.dp,
                backgroundColor = Color(color)
            ) {
                Box {
                    ItemImage(icons, modifier = Modifier.align(Alignment.Center))
                    Text(
                        "PREVIEW",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 12.dp)
                            .clip(RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp))
                            .background(Color(0xFF5DDAD0))
                            .padding(start = 12.dp, end = 4.dp)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }
    }
}