package com.lehtimaeki.askold.landingscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.billingclient.api.*
import com.lehtimaeki.askold.theme.MyApplicationTheme
import com.lehtimaeki.askold.ColorPalettes
import com.lehtimaeki.askold.FullscreenActivity
import com.lehtimaeki.askold.FullscreenActivity.Companion.ICON_SET_EXTRA_ID
import com.lehtimaeki.askold.IapRepo.IapRepo
import com.lehtimaeki.askold.R
import com.lehtimaeki.askold.iconset.IconSet
import com.lehtimaeki.askold.previewscreen.PreviewScreenFragment
import java.io.Serializable

class LandingScreenFragment : Fragment() {

    companion object {
        private const val BABY_NAME = "babyname"

        fun newInstance(name: String): LandingScreenFragment {
            val ret = LandingScreenFragment()

            ret.arguments = Bundle().apply {
                putString(BABY_NAME, name)
            }

            return ret
        }
    }

    private val viewModel: LandingScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        IapRepo.startConnection()
        return ComposeView(requireContext()).apply {
            setContent {
                val freeIconSets by viewModel.iconSets.collectAsState()
                val paidIconSets by IapRepo.paidIconSetsFlow.collectAsState()
                val iconSets = arrayListOf<IconSetWrapper>()
                iconSets.addAll(freeIconSets)
                iconSets.addAll(paidIconSets)

                MyApplicationTheme {
                    ItemsList(iconSets)
                }
            }
        }
    }

    @Composable
    fun TitleText(
        text: String, iconSetWrapper: IconSetWrapper
    ) {
        if (iconSetWrapper.customText) {
            Text(
                text,
                color = Color(0xFF8674F5),
                style = MaterialTheme.typography.h1,
                modifier = Modifier
                    .padding(bottom = 16.dp),
            )
            BabyNameText(arguments?.getString(BABY_NAME).toString())
        } else {
            Text(
                text,
                color = Color.Gray,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }

    @Composable
    fun BabyNameText(name: String) {
        Text(
            name,
            color = Color.Gray,
            fontSize = 35.sp,
            modifier = Modifier
                .padding(bottom = 16.dp),
        )
    }

    @Composable
    fun ItemNameText(
        text: String
    ) {
        Text(
            text,
            color = Color.Gray,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 24.dp, bottom = 20.dp),
            fontWeight = FontWeight.SemiBold
        )
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
    fun ItemTypeText(text: String, modifier: Modifier = Modifier) {
        Text(
            text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .wrapContentSize()
                .padding(top = 12.dp)
                .clip(RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp))
                .background(Color(0xFF8674F5))
                .padding(start = 12.dp, end = 4.dp)
        )
    }

    @Composable
    fun ItemsList(
        iconSetsWrapper: List<IconSetWrapper>
    ) {
        Box(modifier = Modifier.background(Color.White)) {
            Image(
                modifier = Modifier
                    .padding(bottom = 143.dp)
                    .fillMaxWidth(),
                painter = painterResource(R.drawable.bg),
                contentDescription = "background_image",
            )
            Image(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 34.dp),
                painter = painterResource(R.drawable.baby),
                contentDescription = "baby_image",
                colorFilter = ColorFilter.tint(color = Color.White)
            )
            Column {
                Image(
                    modifier = Modifier
                        .size(70.dp)
                        .padding(start = 32.dp)
                        .fillMaxWidth(),
                    painter = painterResource(R.drawable.baby),
                    contentDescription = "baby_image",
                )
                LazyVerticalGrid(
                    modifier = Modifier
                        .wrapContentWidth()
                        .background(Color.Transparent),
                    columns = GridCells.Adaptive(140.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    contentPadding = PaddingValues(start = 32.dp, end = 32.dp)
                ) {
                    items(iconSetsWrapper, span = { item ->
                        val spanCount = if (item.iconSet == null) 2 else 1
                        GridItemSpan(spanCount)
                    }) { iconSetWrapper -> Item(iconSetWrapper) }
                }
            }
        }
    }

    @Composable
    fun Item(iconSetWrapper: IconSetWrapper) {
        if (iconSetWrapper.iconSet == null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                iconSetWrapper.label?.let { TitleText(it, iconSetWrapper) }
            }
        } else {
            val color =
                ColorPalettes.getNextColorFromPalette(iconSetWrapper.iconSet.useLightPalette)
            Column {
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .size(160.dp)
                        .clickable(onClick = { navigateToFullScreenActivity(iconSetWrapper) }),
                    elevation = 8.dp,
                    backgroundColor = Color(color)
                ) {
                    val text = if (iconSetWrapper.iconSet.isUnlocked) "FREE" else "PAID"
                    Box {
                        ItemImage(
                            iconSetWrapper.iconSet.icons.firstOrNull(),
                            modifier = Modifier.align(Alignment.Center)
                        )
                        ItemTypeText(text = text, modifier = Modifier.align(Alignment.TopEnd))
                    }
                }
                ItemNameText(iconSetWrapper.iconSet.name)
            }
        }
    }

    private fun navigateToFullScreenActivity(iconSetWrapper: IconSetWrapper) {
        if (iconSetWrapper.iconSet?.isUnlocked == true) {
            activity?.startActivity(
                Intent(
                    context,
                    FullscreenActivity::class.java
                ).apply {
                    putExtras(Bundle().apply {
                        putInt(ICON_SET_EXTRA_ID, iconSetWrapper.iconSet.id)
                    })
                })
        } else {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.container, PreviewScreenFragment.newInstance(iconSetWrapper))
                ?.addToBackStack(null)
                ?.commit()
        }
    }
}

data class IconSetWrapper(
    val id: Int,
    val iconSet: IconSet?,
    val label: String?,
    val customText: Boolean,
    val paidProductDetails: ProductDetails? = null
) : Serializable

// TODO
//            if(dataSet[position].iconSet?.tintForContrast == true){
//                ImageViewCompat.setImageTintList(
//                    viewHolder.iconView,
//                    ColorStateList.valueOf(ColorPalettes.getContrastColor(color))
//                )