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


    companion object {
        fun fromKingdomId(catId: Int): KingdomType{
            KingdomType.entries.forEach { cat ->
                if(cat.kingdomId == catId) return cat
            }
            return Animals
        }
    }
}