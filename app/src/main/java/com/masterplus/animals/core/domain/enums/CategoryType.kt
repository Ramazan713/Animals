package com.masterplus.animals.core.domain.enums

import com.masterplus.animals.core.domain.models.CategoryDataType

enum class CategoryType(
    val catId: Int,
    val title: String
) {
    Habitat(
        catId = 1,
        title = "Yaşam Alanları"
    ),
    Class(
        catId = 2,
        title = "Sınıflar"
    ),
    Order(
        catId = 3,
        title = "Takımlar"
    ),
    Family(
        catId = 4,
        title = "Familyalar"
    ),
    List(
        catId = 5,
        title = "Listeler"
    );


    fun toChildType(): CategoryType?{
        return when(this){
            Habitat -> null
            Class -> Order
            Order -> Family
            Family -> null
            List -> null
        }
    }

    fun toCategoryDataType(): CategoryDataType{
        return when(this){
            Habitat -> CategoryDataType.Habitat
            Class -> CategoryDataType.Class
            Order -> CategoryDataType.Order
            Family -> CategoryDataType.Family
            List -> CategoryDataType.List
        }
    }

    fun toListIdControlOrNull(categoryItemId: Int?): Int?{
        if (this == List) return categoryItemId
        return null
    }

    companion object {
        fun fromCatId(catId: Int): CategoryType{
            entries.forEach { cat ->
                if(cat.catId == catId) return cat
            }
            return Habitat
        }
    }
}