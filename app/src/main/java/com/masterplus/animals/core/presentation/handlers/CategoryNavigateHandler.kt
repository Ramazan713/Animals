package com.masterplus.animals.core.presentation.handlers

import com.masterplus.animals.core.domain.enums.CategoryType

fun categoryNavigateHandler(
    itemId: Int?,
    categoryType: CategoryType,
    onNavigateToSpeciesList: (CategoryType, Int?) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, Int) -> Unit,
){
    if(categoryType == CategoryType.Family){
        onNavigateToSpeciesList(categoryType, itemId)
    }else{
        onNavigateToCategoryListWithDetail(categoryType, itemId ?: 0)
    }
}