package com.masterplus.animals.features.search.presentation.app_search

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.extentions.isEmptyResult
import com.masterplus.animals.core.extentions.isLoading
import com.masterplus.animals.core.extentions.isNotEmptyResult
import com.masterplus.animals.core.presentation.components.DefaultAnimatedVisibility
import com.masterplus.animals.core.presentation.components.ImageCategoryDataRow
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData

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
                SearchResultAnimatedVisibility(
                    speciesPagingData.isNotEmptyResult()
                ) {
                    ImageCategoryDataRow(
                        modifier = Modifier
                            .padding(bottom = itemSpacing),
                        title = "Türler",
                        showMore = false,
                        pagingItems = speciesPagingData,
                        useTransition = true,
                        contentPaddings = lazyItemContentPaddings,
                        onClickItem = { item ->
                            onNavigateToSpeciesDetail(item.id)
                        }
                    )

                }
            }

            item {
                SearchResultAnimatedVisibility(
                    classPagingData.isNotEmptyResult()
                ) {
                    ImageCategoryDataRow(
                        modifier = Modifier
                            .padding(bottom = itemSpacing),
                        title = "Sınıflar",
                        showMore = false,
                        useTransition = true,
                        pagingItems = classPagingData,
                        contentPaddings = lazyItemContentPaddings,
                        onClickItem = { item ->
                            onNavigateToCategoryListWithDetail(CategoryType.Class, item.kingdomType, item.id)
                        }
                    )
                }
            }

            item {
                SearchResultAnimatedVisibility(
                    orderPagingData.isNotEmptyResult()
                ) {
                    ImageCategoryDataRow(
                        modifier = Modifier
                            .padding(bottom = itemSpacing),
                        title = "Takımlar",
                        showMore = false,
                        useTransition = true,
                        pagingItems = orderPagingData,
                        contentPaddings = lazyItemContentPaddings,
                        onClickItem = { item ->
                            onNavigateToCategoryListWithDetail(CategoryType.Order, item.kingdomType, item.id)
                        }
                    )
                }

            }

            item {
                SearchResultAnimatedVisibility(
                    familyPagingData.isNotEmptyResult()
                ) {
                    ImageCategoryDataRow(
                        modifier = Modifier
                            .padding(bottom = itemSpacing),
                        title = "Familyalar",
                        showMore = false,
                        useTransition = true,
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

@Composable
private fun SearchResultAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable() (AnimatedVisibilityScope.() -> Unit)
) {
    DefaultAnimatedVisibility(
        visible,
        modifier = modifier,
        enter = fadeIn() + slideInHorizontally { -it },
        exit = fadeOut() + slideOutHorizontally { -it },
        content = content
    )
}


@Preview(showBackground = true)
@Composable
private fun SearchResultPageContentPreview() {
    SearchResultPageContent(
        speciesPagingData = getPreviewLazyPagingData(listOf(
            SampleDatas.categoryData, SampleDatas.categoryData.copy(id = 3), SampleDatas.categoryData.copy(id = 4),
            SampleDatas.categoryData.copy(id = 5)
        )),
        familyPagingData = getPreviewLazyPagingData(listOf(SampleDatas.categoryData)),
        classPagingData = getPreviewLazyPagingData(listOf()),
        orderPagingData = getPreviewLazyPagingData(listOf()),
        state = AppSearchState(),
        onNavigateToSpeciesList = {x,y,z,t->},
        onNavigateToSpeciesDetail = {},
        onNavigateToCategoryListWithDetail = {x,y,z->},

    )
}