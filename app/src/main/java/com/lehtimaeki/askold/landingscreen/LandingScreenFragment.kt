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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import com.lehtimaeki.askold.*
import com.lehtimaeki.askold.theme.AskoldTheme
import com.lehtimaeki.askold.FullscreenActivity.Companion.ICON_SET_EXTRA_ID
import com.lehtimaeki.askold.R
import com.lehtimaeki.askold.iapRepo.InAppPurchasesRep
import com.lehtimaeki.askold.R.*
import com.lehtimaeki.askold.iconset.IconSetWrapper
import com.lehtimaeki.askold.previewscreen.PreviewScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LandingScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        InAppPurchasesRep.startConnection()
        return ComposeView(requireContext()).apply {
            setContent {
                val viewModel: LandingScreenViewModel = hiltViewModel()
                val iconSets by viewModel.iconSets.collectAsState()
                AskoldTheme {
                    IconsCategoriesList(iconSets)
                }
            }
        }
    }

    @Composable
    fun UserGreetingTitle(
        helloTextSize: Int,
        nameTextSize: Int,
        textSize: Int,
        text: String, iconSetWrapper: IconSetWrapper
    ) {
        if (iconSetWrapper.customText) {
            Text(
                text = text,
                color = colorResource(id = color.purple_color),
                fontSize = helloTextSize.sp,
                style = MaterialTheme.typography.h1,
                modifier = Modifier
                    .padding(bottom = dimensionResource(dimen.spacing_normal)),
            )
            UserNameText(nameTextSize, arguments?.getString(BABY_NAME).toString())
        } else {
            Text(
                text = text,
                color = Color(0xFF666666),
                fontSize = textSize.sp,
                modifier = Modifier
                    .padding(bottom = dimensionResource(dimen.spacing_normal)),
                fontWeight = FontWeight.Normal
            )
        }
    }

    @Composable
    fun UserNameText(textSize: Int, name: String) {
        Text(
            name,
            color = Color(0xFF666666),
            fontSize = textSize.sp,
            modifier = Modifier
                .padding(bottom = dimensionResource(dimen.spacing_normal)),
        )
    }

    @Composable
    fun ItemCategoryText(
        categoryTextSize: Int,
        text: String
    ) {
        Text(
            text,
            color = Color(0xFF666666),
            fontSize = categoryTextSize.sp,
            modifier = Modifier.padding(bottom = 20.dp),
            style = MaterialTheme.typography.body1
        )
    }

    @Composable
    fun ItemImage(
        imageId: Int?, modifier: Modifier
    ) {
        imageId?.let {
            Image(
                painter = painterResource(it),
                contentDescription = "",
                modifier = modifier
            )
        }
    }

    @Composable
    fun ItemTypeText(
        typeTextSize: Int,
        paddingSize: Int,
        text: String,
        modifier: Modifier = Modifier
    ) {
        Text(
            text,
            color = Color.White,
            fontSize = typeTextSize.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .wrapContentSize()
                .padding(top = paddingSize.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = dimensionResource(dimen.spacing_small),
                        bottomStart = dimensionResource(dimen.spacing_small)
                    )
                )
                .background(
                    colorResource(
                        id = if (text == "PAID") color.purple_color else color.default_tile_color
                    )
                )
                .padding(top = 2.5.dp, bottom = 2.5.dp, start = 14.dp, end = 10.dp)
        )
    }

    @Composable
    fun IconsCategoriesList(
        iconSetsWrapper: List<IconSetWrapper>
    ) {
        val windowInfo = rememberWindowInfo()
        val windowType =
            if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) Size.MOBILE else Size.TABLET
        Box(modifier = Modifier.background(Color.White)) {
            Image(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = windowType.imagePadding.dp)
                    .fillMaxWidth(),
                painter = painterResource(drawable.bg),
                contentDescription = "background image of an icon set",
            )
            Image(
                modifier = Modifier
                    .size(windowType.babyImageSize.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = windowType.offsetSize.dp),
                painter = painterResource(drawable.baby),
                contentDescription = "baby image",
                colorFilter = ColorFilter.tint(color = Color.White)
            )
            IconsCategories(
                helloTextSize = windowType.helloTextSize,
                typeTextSize = windowType.typeTextSize,
                paddingSize = windowType.paddingSize,
                roundedSize = windowType.roundedSize,
                cardSize = windowType.cardSize,
                gridCellSize = windowType.gridCellSize,
                categoryTextSize = windowType.categoryTextSize,
                nameTextSize = windowType.nameTextSize,
                textSize = windowType.textSize,
                imageSize = windowType.imageSize,
                iconSetsWrapper = iconSetsWrapper
            )
        }
    }

    @Composable
    fun IconsCategories(
        helloTextSize: Int,
        typeTextSize: Int,
        paddingSize: Int,
        roundedSize: Int,
        cardSize: Int,
        gridCellSize: Int,
        categoryTextSize: Int,
        nameTextSize: Int,
        textSize: Int,
        imageSize: Int,
        iconSetsWrapper: List<IconSetWrapper>
    ) {
        Column {
            Image(
                modifier = Modifier
                    .size(imageSize.dp)
                    .padding(start = dimensionResource(dimen.spacing_large))
                    .fillMaxWidth(),
                painter = painterResource(drawable.baby),
                contentDescription = "baby image",
            )
            Box(contentAlignment = Alignment.Center) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.Center)
                        .background(Color.Transparent),
                    columns = GridCells.Adaptive(gridCellSize.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp
                    )
                ) {
                    items(iconSetsWrapper, span = { item ->
                        val spanCount = if (item.iconSet == null) 2 else 1
                        GridItemSpan(spanCount)
                    }) { iconSetWrapper ->
                        IconCategory(
                            helloTextSize = helloTextSize,
                            typeTextSize = typeTextSize,
                            paddingSize = paddingSize,
                            roundedSize = roundedSize,
                            cardSize = cardSize,
                            categoryTextSize = categoryTextSize,
                            nameTextSize = nameTextSize,
                            textSize = textSize,
                            iconSetWrapper
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun IconCategory(
        helloTextSize: Int,
        typeTextSize: Int,
        paddingSize: Int,
        roundedSize: Int,
        cardSize: Int,
        categoryTextSize: Int,
        nameTextSize: Int,
        textSize: Int,
        iconSetWrapper: IconSetWrapper
    ) {
        if (iconSetWrapper.iconSet == null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                iconSetWrapper.label?.let {
                    UserGreetingTitle(
                        helloTextSize = helloTextSize,
                        nameTextSize = nameTextSize,
                        textSize = textSize,
                        it,
                        iconSetWrapper
                    )
                }
            }
        } else {
            Column {
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp)
                        .clip(RoundedCornerShape(roundedSize.dp))
                        .fillMaxSize()
                        .clickable(onClick = { navigateToFullScreenActivity(iconSetWrapper) }),
                    elevation = 8.dp,
                    backgroundColor = Color(iconSetWrapper.colorId)
                ) {
                    val text = if (iconSetWrapper.iconSet.isUnlocked) "FREE" else "PAID"
                    Box {
                        ItemImage(
                            iconSetWrapper.iconSet.icons.firstOrNull(),
                            modifier = Modifier.align(Alignment.Center)
                        )
                        ItemTypeText(
                            typeTextSize = typeTextSize,
                            paddingSize = paddingSize,
                            text = text,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                    }
                }
                ItemCategoryText(categoryTextSize = categoryTextSize, iconSetWrapper.iconSet.name)
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

    companion object {
        private const val BABY_NAME = "babyname"

        fun newInstance(name: String): LandingScreenFragment {
            val fragment = LandingScreenFragment()
            fragment.arguments = Bundle().apply {
                putString(BABY_NAME, name)
            }
            return fragment
        }
    }
}

enum class Size(
    val imagePadding: Int,
    val babyImageSize: Int,
    val offsetSize: Int,
    val helloTextSize: Int,
    val typeTextSize: Int,
    val paddingSize: Int,
    val roundedSize: Int,
    val cardSize: Int,
    val gridCellSize: Int,
    val categoryTextSize: Int,
    val nameTextSize: Int,
    val textSize: Int,
    val imageSize: Int,
) {
    MOBILE(
        imagePadding = 130,
        babyImageSize = 150,
        offsetSize = 34,
        helloTextSize = 40,
        typeTextSize = 14,
        paddingSize = 16,
        roundedSize = 17,
        cardSize = 160,
        gridCellSize = 140,
        categoryTextSize = 22,
        nameTextSize = 35,
        textSize = 24,
        imageSize = 70,
    ),
    TABLET(
        imagePadding = 145,
        babyImageSize = 250,
        offsetSize = 84,
        helloTextSize = 75,
        typeTextSize = 32,
        paddingSize = 24,
        roundedSize = 32,
        cardSize = 300,
        gridCellSize = 280,
        categoryTextSize = 40,
        nameTextSize = 66,
        textSize = 50,
        imageSize = 100,
    )
}