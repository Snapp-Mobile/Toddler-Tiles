package com.lehtimaeki.askold.iapRepo

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import com.android.billingclient.api.*
import com.lehtimaeki.askold.ColorPalettes
import com.lehtimaeki.askold.MyApplication
import com.lehtimaeki.askold.iconset.IconSet
import com.lehtimaeki.askold.iconset.IconSetRepo
import com.lehtimaeki.askold.iconset.IconSetWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


object InAppPurchasesRep {
    val paidIconSetsFlow = MutableStateFlow<List<IconSetWrapper>>(emptyList())

    private val purchaseUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            Log.v("TAG_INAPP", "billingResult responseCode : ${billingResult.responseCode}")

            when {
                billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null -> {
                    for (purchase in purchases) {
                        for (paidIconSet: IconSet in IconSetRepo.paidIconSets) {
                            if (purchase.products[0] == paidIconSet.id.toString()) {
                                handleNonConsumablePurchase(purchase, paidIconSet.id)
                            }
                        }
                    }
                }
                billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED -> {
                    // Handle an error caused by a user cancelling the purchase flow.
                }
                else -> {
                    // Handle any other error codes.
                }
            }
        }

    private var billingClient = MyApplication.getAppContext()?.let {
        BillingClient.newBuilder(it)
            .setListener(purchaseUpdateListener)
            .enablePendingPurchases()
            .build()
    }

    fun startConnection() {
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
        unlockPaidIconSet(iconSetId)
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
                            .setProductId(ID_PAID_WOODLANDS)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build(),
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(ID_PAID_AFRICAN_ANIMALS)
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
                paidIconSetsList.add(
                    IconSetWrapper(
                        Int.MAX_VALUE,
                        null,
                        "Buy more fun sets",
                        false
                    )
                )

                for (product: ProductDetails in productDetailsList) {
                    Log.v("TAG_INAPP", "product : $product")
                    for (paidIconSet: IconSet in IconSetRepo.paidIconSets) {
                        if (paidIconSet.id.toString() == product.productId) {
                            paidIconSetsList.add(
                                IconSetWrapper(
                                    product.productId.toInt(),
                                    paidIconSet,
                                    null,
                                    false,
                                    product,
                                    ColorPalettes.getNextColorFromPaletteCompose(paidIconSet.useLightPalette)
                                )
                            )
                        }
                    }
                    // check if purchased or not
                    val params = QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.INAPP)
                    CoroutineScope(Dispatchers.IO).launch {
                        val purchasesResult = billingClient?.queryPurchasesAsync(params.build())
                        purchasesResult?.purchasesList?.forEach { purchase ->
                            purchase.products.forEach { purchasedProductId ->
                                val matchedIconSet =
                                    paidIconSetsList.find { it.id == purchasedProductId.toInt() }
                                matchedIconSet?.iconSet?.isUnlocked = true

                            }
                        }
                        addPaidIconSets(paidIconSetsList)
                    }
                }
            }
        }
    }

    fun navigateToPayment(activity: FragmentActivity?, iconSetWrapper: IconSetWrapper?) {
        val product = iconSetWrapper?.paidProductDetails
        product?.let {
            val productDetailsParamsList = listOf(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(product)
                    .build()
            )
            val billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build()
            if (activity != null) {
                billingClient?.launchBillingFlow(activity, billingFlowParams)?.responseCode
            }
        }
    }

    private fun addPaidIconSets(paidIconSets: ArrayList<IconSetWrapper>) {
        paidIconSetsFlow.value = paidIconSets
    }

    private fun unlockPaidIconSet(paidIconSetId: Int) {
        val iconList = paidIconSetsFlow.value

        val updatedList: List<IconSetWrapper> = iconList.toMutableList().apply {
            val savedIconSetWrapper = this.find { it.id == paidIconSetId }
            val indexOfIconSetWrapper = this.indexOf(savedIconSetWrapper)
            if (savedIconSetWrapper != null) {
                this.remove(savedIconSetWrapper)
                val unlockedIconSet = savedIconSetWrapper.iconSet?.copy(isUnlocked = true)
                this.add(indexOfIconSetWrapper, savedIconSetWrapper.copy(iconSet = unlockedIconSet))
            }
        }

        paidIconSetsFlow.value = updatedList
    }

    private const val ID_PAID_WOODLANDS = "3"
    private const val ID_PAID_AFRICAN_ANIMALS = "4"
}