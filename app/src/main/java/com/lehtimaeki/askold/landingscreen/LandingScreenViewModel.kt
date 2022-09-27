package com.lehtimaeki.askold.landingscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lehtimaeki.askold.IapRepo.IapRepo
import com.lehtimaeki.askold.iconset.IconSetRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LandingScreenViewModel @Inject constructor() : ViewModel() {

    val iconSets: StateFlow<List<IconSetWrapper>>

    init {
        val freeIconSetsList = ArrayList<IconSetWrapper>()
        freeIconSetsList.add(IconSetWrapper(0, null, "Hello, ", true))
        IconSetRepo.allIconSets.filter { it.isUnlocked }.forEach { iconSet ->
            freeIconSetsList.add(IconSetWrapper(iconSet.id, iconSet, null, true))
        }

        // This is just to test until we will be able to create the paid items in Play Store
        freeIconSetsList.add(IconSetWrapper(Int.MAX_VALUE, null, "Buy more fun sets", false))
        IconSetRepo.paidIconSets.filter { !it.isUnlocked }.forEach { iconSet ->
            freeIconSetsList.add(IconSetWrapper(iconSet.id, iconSet, null, false))
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