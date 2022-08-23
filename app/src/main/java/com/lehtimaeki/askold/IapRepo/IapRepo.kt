package com.lehtimaeki.askold.IapRepo

import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.android.billingclient.api.*
import com.lehtimaeki.askold.MyApplication
import com.lehtimaeki.askold.iconset.IconSet
import com.lehtimaeki.askold.iconset.IconSetRepo
import com.lehtimaeki.askold.landingscreen.IconSetWrapper
import kotlinx.coroutines.flow.MutableStateFlow

object IapRepo {
    var paidIconSetsFlow = MutableStateFlow<List<IconSetWrapper>>(emptyList())

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

                    paidIconSetsList.add(IconSetWrapper(Int.MAX_VALUE, null, "Buy more fun sets", false))
                    for (paidIconSet: IconSet in IconSetRepo.paidIconSets) {
                        if (paidIconSet.id.toString() == product.productId) {
                            paidIconSetsList.add(
                                IconSetWrapper(
                                    product.productId.toInt(),
                                    paidIconSet,
                                    null,
                                    false,
                                    product
                                )
                            )
                        }
                    }
                    addPaidIconSets(paidIconSetsList)
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
        iconList[paidIconSetId].iconSet?.isUnlocked = true
        paidIconSetsFlow.value = iconList
    }
}