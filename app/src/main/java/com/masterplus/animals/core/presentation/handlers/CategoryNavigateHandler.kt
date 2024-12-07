package com.masterplus.animals.core.presentation.handlers

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType

fun categoryNavigateHandler(
    itemId: Int?,
    categoryType: CategoryType,
    kingdomType: KingdomType,
    onNavigateToSpeciesList: (CategoryType, Int?, KingdomType) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, Int, KingdomType) -> Unit,
){
    if(categoryType == CategoryType.Family){
        onNavigateToSpeciesList(categoryType, itemId, kingdomType)
    }else{
        onNavigateToCategoryListWithDetail(categoryType, itemId ?: 0, kingdomType)
    }
}