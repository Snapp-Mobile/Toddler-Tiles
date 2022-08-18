package com.lehtimaeki.askold.previewscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.lehtimaeki.askold.IapRepo.IapRepo.navigateToPayment
import com.lehtimaeki.askold.landingscreen.IconSetWrapper

class PreviewScreenFragment : Fragment() {

    companion object {
        private const val ICON_SET_WRAPPER = "iconset"

        fun newInstance(
            iconSetWrapper: IconSetWrapper
        ): PreviewScreenFragment {
            val ret = PreviewScreenFragment()

            ret.arguments = Bundle().apply {
                iconSetWrapper.let { putSerializable(ICON_SET_WRAPPER, it) }
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
        viewModel.loadIconSet(arguments?.getSerializable(ICON_SET_WRAPPER) as IconSetWrapper?)
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
        icons: List<IconWrapper>
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
    fun Item(icons: IconWrapper) {
        Column {

            Card(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .clickable(onClick = {
                        navigateToPayment(
                            activity,
                            arguments?.getSerializable(
                                ICON_SET_WRAPPER
                            ) as IconSetWrapper?
                        )
                    }),
                elevation = 8.dp,
                backgroundColor = Color(icons.colorId)
            ) {
                Box {
                    ItemImage(icons.iconId, modifier = Modifier.align(Alignment.Center))
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