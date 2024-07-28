package com.masterplus.animals.core.domain.enums

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
    );

    companion object {
        fun fromCatId(catId: Int): CategoryType{
            entries.forEach { cat ->
                if(cat.catId == catId) return cat
            }
            return Habitat
        }
    }
}