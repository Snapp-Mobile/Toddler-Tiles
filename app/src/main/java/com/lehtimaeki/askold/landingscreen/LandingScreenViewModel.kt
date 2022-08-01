package com.lehtimaeki.askold.landingscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.iconset.IconSetRepo

class LandingScreenViewModel : ViewModel() {
    var iconSets = MutableLiveData<List<IconSetWrapper>>(emptyList())

    init {

        val iconSetsList = ArrayList<IconSetWrapper>()
        iconSetsList.add(IconSetWrapper(0, null, "Begin Learning"))
        IconSetRepo.allIconSets.filter { it.isUnlocked }.forEach { iconSet ->
            iconSetsList.add(IconSetWrapper(iconSet.id, iconSet, null))
        }

        iconSetsList.add(IconSetWrapper(Int.MAX_VALUE, null, "Buy more fun sets"))
        IconSetRepo.allIconSets.filter { !it.isUnlocked }.forEach { iconSet ->
            iconSetsList.add(IconSetWrapper(iconSet.id, iconSet, null))
        }

        iconSets.value = iconSetsList
    }
}