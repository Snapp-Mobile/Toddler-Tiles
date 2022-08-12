package com.lehtimaeki.askold.previewscreen

import androidx.lifecycle.ViewModel
import com.lehtimaeki.askold.iconset.IconSet
import com.lehtimaeki.askold.iconset.IconSetRepo
import kotlinx.coroutines.flow.MutableStateFlow

class PreviewScreenViewModel : ViewModel() {

    var icons = MutableStateFlow<List<Int>>(emptyList())
    private val iconSetsList = ArrayList<Int>()

    fun loadIconSet(id: Int?) {

        for (paidIconSet: IconSet in IconSetRepo.paidIconSets) {
            if (paidIconSet.id == id) {
                iconSetsList.addAll(paidIconSet.icons)
            }
        }
        icons.value = iconSetsList
    }
}