package com.lehtimaeki.askold.landingscreen

import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.ColorPalettes
import com.lehtimaeki.askold.MyApplication
import com.lehtimaeki.askold.R
import com.lehtimaeki.askold.iapRepo.InAppPurchasesRep
import com.lehtimaeki.askold.iconset.IconSetRepo
import com.lehtimaeki.askold.iconset.IconSetWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LandingScreenViewModel @Inject constructor() : ViewModel() {

    val iconSets: Flow<List<IconSetWrapper>> =
        InAppPurchasesRep.paidIconSetsFlow.map { paidIconSets ->
            val freeIconSetsList = ArrayList<IconSetWrapper>()

            freeIconSetsList.add(
                IconSetWrapper(
                    id = 0,
                    iconSet = null,
                    label = MyApplication.getAppContext()?.getString(R.string.hello),
                    customText = true
                )
            )

            IconSetRepo.freeIconSets
                .filter { it.isUnlocked }
                .forEach { iconSet ->
                    freeIconSetsList.add(
                        IconSetWrapper(
                            id = iconSet.id,
                            iconSet = iconSet,
                            label = null,
                            customText = true,
                            colorId = ColorPalettes.getNextColorFromPaletteCompose(iconSet.useLightPalette)
                        )
                    )
                }

            // Store all the bought icon sets
            val purchasedPaidAssets = paidIconSets.filter { it.iconSet?.isUnlocked == true }

            // Store all the paid icon sets which weren't bought
            val toBePurchasedPaidAssets =
                paidIconSets.filter { it.iconSet?.isUnlocked == false || it.iconSet == null }

            // If size of the unpaid icon sets is 1, it means that all paid icon sets were bought
            // and the element found in the list is the label
            // We need to add all the bought items to the free ones
            if (toBePurchasedPaidAssets.size == 1) {
                return@map freeIconSetsList + purchasedPaidAssets
            }

            return@map freeIconSetsList + purchasedPaidAssets + toBePurchasedPaidAssets
        }
}