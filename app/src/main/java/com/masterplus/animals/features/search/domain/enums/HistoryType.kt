package com.masterplus.animals.features.search.domain.enums

enum class HistoryType(
    val typeId: Int
) {
    Content(
        typeId = 1
    ),
    Category(
        typeId = 2
    ),
    App(
        typeId = 3
    );

    companion object {
        fun fromTypeId(typeId: Int): HistoryType{
            for(type in HistoryType.entries){
                if(type.typeId == typeId) return type
            }
            return Content
        }
    }
}