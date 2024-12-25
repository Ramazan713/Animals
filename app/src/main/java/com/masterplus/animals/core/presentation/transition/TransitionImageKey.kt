package com.masterplus.animals.core.presentation.transition

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import kotlinx.serialization.Serializable

@Serializable
data class TransitionImageKey(
    val id: Int,
    val imageType: TransitionImageType,
    val kingdomType: KingdomType,
    val extra: String? = null
)


enum class TransitionImageType{
    Class,
    Family,
    Order,
    Habitat,
    Species,
    SpeciesImages
    ;


    companion object {
        fun fromCategoryType(categoryType: CategoryType): TransitionImageType?{
            return when(categoryType){
                CategoryType.Habitat -> Habitat
                CategoryType.Class -> Class
                CategoryType.Order -> Order
                CategoryType.Family -> Family
                CategoryType.List -> null
            }
        }
    }
}