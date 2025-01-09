package com.masterplus.animals.features.search.presentation.app_search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.extentions.isEmptyResult
import com.masterplus.animals.core.extentions.isLoading
import com.masterplus.animals.core.extentions.isNotEmptyResult
import com.masterplus.animals.core.presentation.components.ImageCategoryDataRow
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent

private val itemSpacing = 16.dp

@Composable
fun SearchResultPageContent(
    state: AppSearchState,
    classPagingData: LazyPagingItems<CategoryData>,
    orderPagingData: LazyPagingItems<CategoryData>,
    familyPagingData: LazyPagingItems<CategoryData>,
    speciesPagingData: LazyPagingItems<CategoryData>,
    onNavigateToCategoryListWithDetail: (CategoryType, KingdomType, Int) -> Unit,
    onNavigateToSpeciesList: (CategoryType, KingdomType, Int?, Int) -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPaddings: PaddingValues = PaddingValues(),
    lazyItemContentPaddings: PaddingValues = PaddingValues()
) {
    val isEmptyResult by remember(
        classPagingData.loadState, orderPagingData.loadState, familyPagingData.loadState,
        speciesPagingData.loadState
    ) {
        derivedStateOf {
            classPagingData.isEmptyResult() && orderPagingData.isEmptyResult() &&
                    familyPagingData.isEmptyResult() && speciesPagingData.isEmptyResult()
        }
    }

    val isLoading by remember(
        classPagingData.loadState, orderPagingData.loadState, familyPagingData.loadState,
        speciesPagingData.loadState, state.isRemoteSearching, state.searchType
    ) {
        derivedStateOf {
            val isLocalAnyLoading = classPagingData.isLoading() || orderPagingData.isLoading() ||
                    familyPagingData.isLoading() || speciesPagingData.isLoading()
            (isLocalAnyLoading && state.searchType.isLocal) || (state.isRemoteSearching && state.searchType.isServer)
        }
    }

    SharedLoadingPageContent(
        modifier = modifier,
        isLoading = isLoading,
        overlayLoading = true,
        isEmptyResult = isEmptyResult
    ){
        LazyColumn(
            contentPadding = contentPaddings,
            modifier = Modifier.matchParentSize()
        ) {
            item {
                AnimatedVisibility(
                    speciesPagingData.isNotEmptyResult()
                ) {
                    ImageCategoryDataRow(
                        modifier = Modifier
                            .padding(bottom = itemSpacing),
                        title = "Türler",
                        showMore = false,
                        pagingItems = speciesPagingData,
                        contentPaddings = lazyItemContentPaddings,
                        onClickItem = { item ->
                            onNavigateToSpeciesDetail(item.id)
                        }
                    )

                }
            }

            item {
                AnimatedVisibility(
                    classPagingData.isNotEmptyResult()
                ) {
                    ImageCategoryDataRow(
                        modifier = Modifier
                            .padding(bottom = itemSpacing),
                        title = "Sınıflar",
                        showMore = false,
                        showMoreItem = false,
                        pagingItems = classPagingData,
                        contentPaddings = lazyItemContentPaddings,
                        onClickItem = { item ->
                            onNavigateToCategoryListWithDetail(CategoryType.Class, item.kingdomType, item.id)
                        }
                    )
                }
            }

            item {
                AnimatedVisibility(
                    orderPagingData.isNotEmptyResult()
                ) {
                    ImageCategoryDataRow(
                        modifier = Modifier
                            .padding(bottom = itemSpacing),
                        title = "Takımlar",
                        showMore = false,
                        pagingItems = orderPagingData,
                        contentPaddings = lazyItemContentPaddings,
                        onClickItem = { item ->
                            onNavigateToCategoryListWithDetail(CategoryType.Order, item.kingdomType, item.id)
                        }
                    )
                }

            }

            item {
                AnimatedVisibility(
                    familyPagingData.isNotEmptyResult()
                ) {
                    ImageCategoryDataRow(
                        modifier = Modifier
                            .padding(bottom = itemSpacing),
                        title = "Familyalar",
                        showMore = false,
                        pagingItems = familyPagingData,
                        contentPaddings = lazyItemContentPaddings,
                        onClickItem = { item ->
                            onNavigateToSpeciesList(CategoryType.Family, item.kingdomType, item.id,0)
                        }
                    )
                }
            }
        }
    }


}