package com.masterplus.animals.core.shared_features.savepoint.data.mapper

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination


fun SavePointDestination.toCategoryType(): CategoryType?{
    return when(this){
        is SavePointDestination.All -> null
        is SavePointDestination.ClassType -> CategoryType.Class
        is SavePointDestination.Family -> CategoryType.Family
        is SavePointDestination.Habitat -> CategoryType.Habitat
        is SavePointDestination.ListType -> CategoryType.List
        is SavePointDestination.Order -> CategoryType.Order
    }
}

fun CategoryType.toSavePointDestinationTypeId(itemId: Int?, returnAll: Boolean = true): Int{
    if(itemId == null && returnAll) return SavePointDestination.All.DESTINATION_TYPE_ID
    return when(this){
        CategoryType.Habitat -> SavePointDestination.Habitat.DESTINATION_TYPE_ID
        CategoryType.Class -> SavePointDestination.ClassType.DESTINATION_TYPE_ID
        CategoryType.Order -> SavePointDestination.Order.DESTINATION_TYPE_ID
        CategoryType.Family -> SavePointDestination.Family.DESTINATION_TYPE_ID
        CategoryType.List -> SavePointDestination.ListType.DESTINATION_TYPE_ID
    }
}