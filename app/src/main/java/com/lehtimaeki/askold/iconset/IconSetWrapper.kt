package com.lehtimaeki.askold.iconset

import com.android.billingclient.api.ProductDetails
import java.io.Serializable

data class IconSetWrapper(
    val id: Int,
    val iconSet: IconSet?,
    val label: String?,
    val customText: Boolean,
    val paidProductDetails: ProductDetails? = null,
    val colorId: Int = 0
) : Serializable