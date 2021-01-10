package com.lehtimaeki.askold.landingscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.iconset.IconSet
import com.lehtimaeki.askold.iconset.IconSetRepo

class LandingScreenViewModel : ViewModel() {
    val iconSetsLiveData = MutableLiveData<Pair<List<IconSet>, List<IconSet>>>()

    init {

        iconSetsLiveData.postValue(
            Pair(IconSetRepo.allIconSets.filter { it.isUnlocked },
                IconSetRepo.allIconSets.filter { !it.isUnlocked })
        )

    }
}

