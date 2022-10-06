package com.lehtimaeki.askold.landingscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lehtimaeki.askold.iapRepo.IapRepo
import com.lehtimaeki.askold.iconset.IconSetRepo
import com.lehtimaeki.askold.iconset.IconSetWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LandingScreenViewModel @Inject constructor() : ViewModel() {

    val iconSets: StateFlow<List<IconSetWrapper>>

    init {
        val freeIconSetsList = ArrayList<IconSetWrapper>()
        freeIconSetsList.add(
            IconSetWrapper(
                id = 0,
                iconSet = null,
                label = "Hello, ",
                customText = true
            )
        )
        IconSetRepo.allIconSets.filter { it.isUnlocked }.forEach { iconSet ->
            freeIconSetsList.add(
                IconSetWrapper(
                    id = iconSet.id,
                    iconSet = iconSet,
                    label = null,
                    customText = true
                )
            )
        }

        // This is just to test until we will be able to create the paid items in Play Store
        freeIconSetsList.add(
            IconSetWrapper(
                id = Int.MAX_VALUE,
                iconSet = null,
                label = "Buy more fun sets",
                customText = false
            )
        )
        IconSetRepo.paidIconSets.filter { !it.isUnlocked }.forEach { iconSet ->
            freeIconSetsList.add(
                IconSetWrapper(
                    id = iconSet.id,
                    iconSet = iconSet,
                    label = null,
                    customText = false
                )
            )
        }

        iconSets = flowOf(IapRepo.paidIconSetsFlow).map {
            freeIconSetsList + it.value
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            freeIconSetsList
        )
    }
}