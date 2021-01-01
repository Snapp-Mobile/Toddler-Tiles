package com.lehtimaeki.askold.iconset

import com.lehtimaeki.askold.utils.GameTileResources
import kotlin.random.Random


object IconSetRepo {

    val allIconSets = mutableListOf<IconSet>()
    private val random = Random.Default

    // Pairs of icon drawable and if it should be tinted
    private val allUnlockedIcons by lazy {
        val ret = mutableListOf<Pair<Int, Boolean>>()
        allIconSets.filter { it.isUnlocked }.forEach {iconSet->
            iconSet.icons.forEach { icon ->
                ret.add(Pair(icon, iconSet.tintForContrast))
            }
        }
        ret.toList()
    }


    init {
        allIconSets.add(IconSet(
            id = 1,
            name = "Material Icons",
            isUnlocked = true,
            GameTileResources.MATERIAL_ICONS,
            tintForContrast = true
        ))
        allIconSets.add(IconSet(
            id = 1,
            name = "Flat Icons",
            isUnlocked = true,
            GameTileResources.FLATICON_IMAGE_ICONS,
            tintForContrast = false
        ))
    }


    /**
     *
     * @return pair of icon drawable and if it should be tinted
     */
    fun getRandomIcon(): Pair<Int, Boolean>{
        return allUnlockedIcons[random.nextInt(allUnlockedIcons.size)]
    }



}