package com.lehtimaeki.askold.landingscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.Surface
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
import com.android.billingclient.api.*
import com.lehtimaeki.askold.ColorPalettes
import com.lehtimaeki.askold.FullscreenActivity
import com.lehtimaeki.askold.FullscreenActivity.Companion.ICON_SET_EXTRA_ID
import com.lehtimaeki.askold.R
import com.lehtimaeki.askold.iconset.IconSet
import com.lehtimaeki.askold.iconset.IconSetRepo

class LandingScreenFragment : Fragment() {

    companion object {
        fun newInstance() = LandingScreenFragment()
    }

    private val viewModel: LandingScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        startConnection()
        return ComposeView(requireContext()).apply {
            setContent {
                val iconSets by viewModel.iconSets.collectAsState()
                ItemsList(iconSets)
            }
        }
    }

    @Composable
    fun TitleText(
        text: String
    ) {
        Text(
            text,
            color = Color.Gray,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp, bottom = 16.dp),
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun ItemNameText(
        text: String
    ) {
        Text(
            text,
            color = Color.Gray,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 24.dp),
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
                .background(Color(0xFFa799f8))
                .padding(start = 12.dp, end = 4.dp)
        )
    }

    @Composable
    fun ItemsList(
        iconSetsWrapper: List<IconSetWrapper>
    ) {
        Surface(color = Color.White) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                Modifier.padding(start = 26.dp, end = 20.dp)
            ) {
                items(iconSetsWrapper, span = { item ->
                    val metrics = resources.displayMetrics
                    val width = metrics.widthPixels
                    val numberOfColumns =
                        width / resources.getDimension(R.dimen.minimal_tile_size)
                            .toInt()
                    val spanCount = if (item.iconSet == null) numberOfColumns else 1
                    GridItemSpan(spanCount)
                }) { iconSetWrapper -> Item(iconSetWrapper) }
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
                iconSetWrapper.label?.let { TitleText(it) }
            }
        } else {
            val color =
                ColorPalettes.getNextColorFromPalette(iconSetWrapper.iconSet.useLightPalette)
            Column {
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .size(140.dp)
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
            val product = iconSetWrapper.paidProductDetails
            product?.let {
                val activity = requireActivity()
                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(product)
                        .build()
                )
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
                billingClient?.launchBillingFlow(activity, billingFlowParams)?.responseCode
            }
        }
    }

    private val purchaseUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            Log.v("TAG_INAPP", "billingResult responseCode : ${billingResult.responseCode}")

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    for (paidIconSet: IconSet in IconSetRepo.paidIconSets) {
                        if (purchase.products[0] == paidIconSet.id.toString()) {
                            handleNonConsumablePurchase(purchase, paidIconSet.id)
                        }
                    }
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }

    private var billingClient = context?.let {
        BillingClient.newBuilder(it)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()
    }

    private fun startConnection() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    queryAvailableProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun handleNonConsumablePurchase(purchase: Purchase, iconSetId: Int) {
        viewModel.unlockedPaidIconSet(iconSetId)
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                billingClient?.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    val billingResponseCode = billingResult.responseCode
                    val billingDebugMessage = billingResult.debugMessage

                    Log.v("TAG_INAPP", "response code: $billingResponseCode")
                    Log.v("TAG_INAPP", "debugMessage : $billingDebugMessage")
                }
            }
        }
    }

    private fun queryAvailableProducts() {
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId("paid_animals")
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                    )
                )
                .build()

        billingClient?.queryProductDetailsAsync(queryProductDetailsParams) { billingResult,
                                                                             productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
                && productDetailsList.isNotEmpty()
            ) {
                val paidIconSetsList = ArrayList<IconSetWrapper>()

                for (product: ProductDetails in productDetailsList) {
                    Log.v("TAG_INAPP", "product : $product")

                    paidIconSetsList.add(IconSetWrapper(Int.MAX_VALUE, null, "Buy more fun sets"))
                    for (paidIconSet: IconSet in IconSetRepo.paidIconSets) {
                        if (paidIconSet.id.toString() == product.productId) {
                            paidIconSetsList.add(
                                IconSetWrapper(
                                    product.productId.toInt(),
                                    paidIconSet,
                                    null,
                                    product
                                )
                            )
                        }
                    }
                    viewModel.addPaidIconSets(paidIconSetsList)
                }
            }
        }
    }
}

data class IconSetWrapper(
    val id: Int,
    val iconSet: IconSet?,
    val label: String?,
    val paidProductDetails: ProductDetails? = null
)

// TODO
//            if(dataSet[position].iconSet?.tintForContrast == true){
//                ImageViewCompat.setImageTintList(
//                    viewHolder.iconView,
//                    ColorStateList.valueOf(ColorPalettes.getContrastColor(color))
//                )