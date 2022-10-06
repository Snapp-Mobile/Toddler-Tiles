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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.lehtimaeki.askold.iapRepo.IapRepo.navigateToPayment
import com.lehtimaeki.askold.iconset.IconSetWrapper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewScreenFragment : Fragment() {

    companion object {
        private const val ICON_SET_WRAPPER = "iconset"

        fun newInstance(
            iconSetWrapper: IconSetWrapper
        ): PreviewScreenFragment {
            val ret = PreviewScreenFragment()

            ret.arguments = Bundle().apply {
                putSerializable(ICON_SET_WRAPPER, iconSetWrapper)
            }

            return ret
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val viewModel: PreviewScreenViewModel = hiltViewModel()
                LaunchedEffect(null) {
                    viewModel.loadIconSet(arguments?.getSerializable(ICON_SET_WRAPPER) as IconSetWrapper?)
                }
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
    fun ItemNameText(
        iconSetWrapper: IconSetWrapper?
    ) {
        Text(
            text = iconSetWrapper?.iconSet?.name.toString(),
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }

    @Composable
    fun IconsList(
        icons: List<IconWrapper>
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { ItemNameText(arguments?.getSerializable(ICON_SET_WRAPPER) as IconSetWrapper?) },
                    navigationIcon = {
                        IconButton(onClick = { activity?.supportFragmentManager?.popBackStack() }) {
                            Icon(Icons.Filled.KeyboardArrowLeft, "backIcon")
                        }
                    },
                    backgroundColor = Color.White,
                    actions = {
                        FloatingActionButton(
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .height(38.dp)
                                .width(126.dp)
                                .padding(end = 17.dp),
                            backgroundColor = (Color(0xFF8674F5)),
                            onClick = {
                                navigateToPayment(
                                    activity,
                                    arguments?.getSerializable(
                                        ICON_SET_WRAPPER
                                    ) as IconSetWrapper?
                                )
                            }) {
                            Text(
                                text = "BUY NOW",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    })
            }) {
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
    }

    @Composable
    fun Item(icons: IconWrapper) {
        Column {

            Card(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxWidth(),
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