package com.masterplus.animals.features.search.presentation.app_search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.extentions.isNotEmptyResult
import com.masterplus.animals.core.presentation.components.ImageCategoryDataRow

private val showMoreCount = 3

@Composable
fun SearchResultPageContent(
    classPagingData: LazyPagingItems<CategoryData>,
    orderPagingData: LazyPagingItems<CategoryData>,
    familyPagingData: LazyPagingItems<CategoryData>,
    speciesPagingData: LazyPagingItems<CategoryData>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    //TODO: Add Empty Component
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        item {
            AnimatedVisibility(
                speciesPagingData.isNotEmptyResult()
            ) {
                ImageCategoryDataRow(
                    title = "Türler",
                    showMore = speciesPagingData.itemCount > showMoreCount,
                    showMoreItem = false,
                    pagingItems = speciesPagingData,
                    onClickItem = {

                    }
                )
            }
        }

        item {
            AnimatedVisibility(
                classPagingData.isNotEmptyResult()
            ) {
                ImageCategoryDataRow(
                    title = "Sınıflar",
                    showMore = classPagingData.itemCount > showMoreCount,
                    showMoreItem = false,
                    pagingItems = classPagingData,
                    onClickItem = {

                    }
                )
            }
        }

        item {
            AnimatedVisibility(
                orderPagingData.isNotEmptyResult()
            ) {
                ImageCategoryDataRow(
                    title = "Takımlar",
                    showMore = orderPagingData.itemCount > showMoreCount,
                    showMoreItem = false,
                    pagingItems = orderPagingData,
                    onClickItem = {

                    }
                )
            }

        }

        item {
            AnimatedVisibility(
                familyPagingData.isNotEmptyResult()
            ) {
                ImageCategoryDataRow(
                    title = "Familyalar",
                    showMore = familyPagingData.itemCount > showMoreCount,
                    showMoreItem = false,
                    pagingItems = familyPagingData,
                    onClickItem = {

                    }
                )
            }
        }
    }
}