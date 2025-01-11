package com.masterplus.animals.core.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.presentation.transition.TransitionImageType

enum class CategoryDataType{
    Habitat,
    Class,
    Order,
    Family,
    List,
    Species;

    fun toTransitionImageType(): TransitionImageType? {
        return when(this){
            Habitat -> TransitionImageType.Habitat
            Class -> TransitionImageType.Class
            Order -> TransitionImageType.Order
            Family -> TransitionImageType.Family
            List -> null
            Species -> TransitionImageType.Species
        }
    }
}

data class CategoryData(
    val id: Int,
    override val orderKey: Int,
    val image: ImageWithMetadata?,
    val title: String,
    val categoryDataType: CategoryDataType,
    val kingdomType: KingdomType,
    val secondaryTitle: String? = null
): ItemOrder{
    val imageUrl get() = image?.imageUrl
}
