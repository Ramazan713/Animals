package com.masterplus.animals.core.domain.enums

enum class KingdomType(
    val kingdomId: Int
) {
    Animals(
        kingdomId = 1
    ),
    Plants(
        kingdomId = 2
    );

    val isAnimals get() = this == Animals
    val isPlants get() = this == Plants

    companion object {
        val DEFAULT = KingdomType.Animals

        fun fromKingdomId(catId: Int): KingdomType{
            return fromKingdomIdOrNull(catId) ?: Animals
        }

        fun fromKingdomIdOrNull(catId: Int?): KingdomType?{
            if(catId == null) return null
            KingdomType.entries.forEach { cat ->
                if(cat.kingdomId == catId) return cat
            }
            return null
        }
    }
}