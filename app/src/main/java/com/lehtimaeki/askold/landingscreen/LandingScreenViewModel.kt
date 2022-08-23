package com.lehtimaeki.askold.landingscreen

import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.iconset.IconSetRepo
import kotlinx.coroutines.flow.MutableStateFlow

class LandingScreenViewModel : ViewModel() {
    var iconSets = MutableStateFlow<List<IconSetWrapper>>(emptyList())
    private val iconSetsList = ArrayList<IconSetWrapper>()

    init {

        iconSetsList.add(IconSetWrapper(0, null, "Hello,",true))
        IconSetRepo.allIconSets.filter { it.isUnlocked }.forEach { iconSet ->
            iconSetsList.add(IconSetWrapper(iconSet.id, iconSet, null,true))
        }

        iconSetsList.add(IconSetWrapper(Int.MAX_VALUE, null, "Buy more fun sets", false))
        IconSetRepo.paidIconSets.filter { !it.isUnlocked }.forEach { iconSet ->
            iconSetsList.add(IconSetWrapper(iconSet.id, iconSet, null,false))
        }

        iconSets.value = iconSetsList
    }
}