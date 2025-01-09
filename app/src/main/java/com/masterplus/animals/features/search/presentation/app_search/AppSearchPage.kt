package com.masterplus.animals.features.search.presentation.app_search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.extentions.clearFocusOnTap
import com.masterplus.animals.core.extentions.plus
import com.masterplus.animals.core.presentation.dialogs.LoadingDialog
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdMobResultHandler
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.core.shared_features.ad.presentation.AdUiResult
import com.masterplus.animals.core.shared_features.ad.presentation.dialogs.ShowAdRequiredDia
import com.masterplus.animals.features.search.presentation.components.SearchField
import com.masterplus.animals.features.search.presentation.components.SearchFilterRow
import com.masterplus.animals.features.search.presentation.components.SearchHistoryColumn
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppSearchPageRoot(
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, KingdomType, Int) -> Unit,
    onNavigateToSpeciesList: (CategoryType, KingdomType, Int?, Int) -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    viewModel: AppSearchViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val classPagingData = viewModel.classResultsPaging.collectAsLazyPagingItems()
    val orderPagingData = viewModel.orderResultsPaging.collectAsLazyPagingItems()
    val speciesPagingData = viewModel.speciesResultsPaging.collectAsLazyPagingItems()
    val familyPagingData = viewModel.familyResultsPaging.collectAsLazyPagingItems()

    AppSearchPage(
        state = state,
        onAction = viewModel::onAction,
        adState = adState,
        onAdAction = onAdAction,
        classPagingData = classPagingData,
        orderPagingData = orderPagingData,
        familyPagingData = familyPagingData,
        speciesPagingData = speciesPagingData,
        onNavigateToSpeciesList = onNavigateToSpeciesList,
        onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
        onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail
    )
}

@Composable
fun AppSearchPage(
    state: AppSearchState,
    onAction: (AppSearchAction) -> Unit,
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, KingdomType, Int) -> Unit,
    onNavigateToSpeciesList: (CategoryType, KingdomType, Int?, Int) -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    classPagingData: LazyPagingItems<CategoryData>,
    orderPagingData: LazyPagingItems<CategoryData>,
    familyPagingData: LazyPagingItems<CategoryData>,
    speciesPagingData: LazyPagingItems<CategoryData>
) {

    val itemContentPaddings = PaddingValues(horizontal = 12.dp)
    val lazyHorizontalPaddings = PaddingValues(horizontal = 12.dp)
    val lazyVerticalPaddings = PaddingValues(bottom = 16.dp, top = 16.dp)


    AdMobResultHandler(
        adUiResult = adState.uiResult,
        onAdAction = onAdAction,
        label = state.adLabel,
        showErrorMessage = true
    ) { adResult ->
        when(adResult){
            is AdUiResult.OnShowingRewardSuccess -> {
                onAction(AppSearchAction.AdShowedSuccess)
            }
            else -> Unit
        }
    }

    ShowLifecycleToastMessage(state.message) {
        onAction(AppSearchAction.ClearMessage)
    }
    
    
    Scaffold(

    ) { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .semantics { isTraversalGroup = true }
                .clearFocusOnTap()
        ){
            GetSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(itemContentPaddings)
                    .padding(vertical = 8.dp),
                state = state,
                onAction = onAction,
            )

            SearchFilterRow(
                modifier = Modifier
                    .padding(itemContentPaddings),
                selectedSearchType = state.searchType,
                onSelectSearchType = { searchType ->
                    onAction(AppSearchAction.SelectSearchType(searchType))
                },
                onRemainingSearchableCount = { state.remainingSearchableCount }
            )

            Crossfade(
                state.query.isBlank(),
                label = "PageContent"
            ) { isQueryEmpty ->
                if(isQueryEmpty){
                    SearchHistoryColumn(
                        modifier = Modifier.fillMaxSize(),
                        histories = state.histories,
                        isLoading = state.historyLoading,
                        contentPaddings = lazyHorizontalPaddings + lazyVerticalPaddings,
                        onHistoryClick = { history ->
                            onAction(AppSearchAction.SearchQuery(history.content))
                        },
                        onDeleteHistoryClick = { history ->
                            onAction(AppSearchAction.DeleteHistory(history))
                        }
                    )
                }else{
                    SearchResultPageContent(
                        contentPaddings = lazyVerticalPaddings,
                        lazyItemContentPaddings = lazyHorizontalPaddings,
                        modifier = Modifier.fillMaxSize(),
                        classPagingData = classPagingData,
                        orderPagingData = orderPagingData,
                        familyPagingData = familyPagingData,
                        speciesPagingData = speciesPagingData,
                        state = state,
                        onNavigateToSpeciesList = onNavigateToSpeciesList,
                        onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
                        onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail
                    )
                }
            }
        }
    }

    state.dialogEvent?.let { dialogEvent ->
        val close = remember { {
            onAction(AppSearchAction.ShowDialog(null))
        } }
        when(dialogEvent){
            AppSearchDialogEvent.ShowAdRequired -> {
                ShowAdRequiredDia(
                    onDismiss = close,
                    onApproved = {
                        onAdAction(AdAction.RequestShowRewardAd(state.adLabel))
                    },
                    title = "Arama limitine ulaştınız"
                )
            }
        }
    }

    if(adState.loadingRewardAd.isLoading && adState.loadingRewardAd.label == state.adLabel){
        LoadingDialog()
    }
}


@Composable
private fun GetSearchBar(
    state: AppSearchState,
    onAction: (AppSearchAction) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchField(
        modifier = modifier,
        query = state.query,
        onValueChange = { onAction(AppSearchAction.SearchQuery(it)) },
        onSearch = {
            if(state.searchType.isServer){
                onAction(AppSearchAction.SearchRemote)
            }else{
                onAction(AppSearchAction.InsertHistory(state.query))
            }
        },
        onClear = {
            onAction(AppSearchAction.InsertHistory(state.query))
            onAction(AppSearchAction.SearchQuery(""))
        },
        placeholder = "Ara",
        searchType = state.searchType,
        isSearching = state.isRemoteSearching
    )
}

@Preview(showBackground = true)
@Composable
private fun AppSearchPreview(modifier: Modifier = Modifier) {
    AppSearchPage(
        state = AppSearchState(
            query = "a",
            histories = listOf(
                SampleDatas.history
            )
        ),
        onAction = {},
        adState = AdState(),
        onAdAction = {},
        onNavigateToSpeciesList = {x,y,z,t->},
        onNavigateToSpeciesDetail = {},
        onNavigateToCategoryListWithDetail = {x,y,z->},
        speciesPagingData = getPreviewLazyPagingData(listOf(
            SampleDatas.categoryData,SampleDatas.categoryData.copy(id = 3), SampleDatas.categoryData.copy(id = 4),
            SampleDatas.categoryData.copy(id = 5)
        )),
        familyPagingData = getPreviewLazyPagingData(listOf(SampleDatas.categoryData)),
        classPagingData = getPreviewLazyPagingData(listOf()),
        orderPagingData = getPreviewLazyPagingData(listOf()),
    )
}