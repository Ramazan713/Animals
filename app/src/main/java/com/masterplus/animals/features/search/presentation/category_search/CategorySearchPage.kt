package com.masterplus.animals.features.search.presentation.category_search


import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.animals.R
import com.masterplus.animals.core.extentions.clearFocusOnTap
import com.masterplus.animals.core.presentation.dialogs.LoadingDialog
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdMobResultHandler
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.core.shared_features.ad.presentation.AdUiResult
import com.masterplus.animals.core.shared_features.ad.presentation.dialogs.ShowAdRequiredDia
import com.masterplus.animals.features.search.domain.enums.SearchType
import com.masterplus.animals.features.search.presentation.components.SearchField
import com.masterplus.animals.features.search.presentation.components.SearchFilterRow
import com.masterplus.animals.features.search.presentation.components.SearchHistoryColumn


@Composable
fun CategorySearchPage(
    state: CategorySearchState,
    onAction: (CategorySearchAction) -> Unit,
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
    onNavigateBack: () -> Unit,
    searchResultContent: @Composable (ColumnScope.(PaddingValues) -> Unit)
) {

    val contentPadding = PaddingValues(
        bottom = 16.dp,
        top = 16.dp
    )

    AdMobResultHandler(
        adUiResult = adState.uiResult,
        onAdAction = onAdAction,
        label = state.adLabel,
        showErrorMessage = true
    ) { adResult ->
        when(adResult){
            is AdUiResult.OnShowingRewardSuccess -> {
                onAction(CategorySearchAction.AdShowedSuccess)
            }
            else -> Unit
        }
    }

    ShowLifecycleToastMessage(state.message) {
        onAction(CategorySearchAction.ClearMessage)
    }

    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(horizontal = 12.dp)
                .semantics { isTraversalGroup = true }
                .clearFocusOnTap()
        ) {
            GetSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                state = state,
                onAction = onAction,
                onNavigateBack = onNavigateBack
            )

            SearchFilterRow(
                modifier = Modifier,
                selectedSearchType = state.searchType,
                onSelectSearchType = { searchType ->
                    onAction(CategorySearchAction.SelectSearchType(searchType))
                },
                onRemainingSearchableCount = { state.remainingSearchableCount }
            )
            Crossfade(
                state.query.isBlank(),
                label = "PageContent"
            ) { isQueryEmpty ->
                if(isQueryEmpty){
                    SearchHistoryColumn(
                        modifier = Modifier.weight(1f),
                        histories = state.histories,
                        isLoading = state.historyLoading,
                        contentPaddings = contentPadding,
                        onHistoryClick = { history ->
                            onAction(CategorySearchAction.SearchQuery(history.content))
                        },
                        onDeleteHistoryClick = { history ->
                            onAction(CategorySearchAction.DeleteHistory(history))
                        }
                    )
                }else{
                    searchResultContent(contentPadding)
                }
            }
        }
    }



    state.dialogEvent?.let { dialogEvent ->
        val close = remember { {
            onAction(CategorySearchAction.ShowDialog(null))
        } }
        when(dialogEvent){
            CategorySearchDialogEvent.ShowAdRequired -> {
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
    state: CategorySearchState,
    onAction: (CategorySearchAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val placeholder = state.titleForPlaceHolder?.asString()?.let { titleForPlaceHolder ->
        stringResource(id = R.string.n_search_in, titleForPlaceHolder)
    } ?: "Ara"
    SearchField(
        modifier = modifier,
        query = state.query,
        onValueChange = { onAction(CategorySearchAction.SearchQuery(it)) },
        onBackPressed = onNavigateBack,
        onSearch = {
            if(state.searchType.isServer){
                onAction(CategorySearchAction.SearchRemote)
            }else{
                onAction(CategorySearchAction.InsertHistory(state.query))
            }
        },
        onClear = {
            onAction(CategorySearchAction.InsertHistory(state.query))
            onAction(CategorySearchAction.SearchQuery(""))
        },
        placeholder = placeholder,
        searchType = state.searchType,
        isSearching = state.isRemoteSearching
    )
}

@Preview(showBackground = true)
@Composable
private fun CategorySearchPagePreview() {
    CategorySearchPage(
        onAdAction = {},
        adState = AdState(),
        state = CategorySearchState(
            searchType = SearchType.Server
        ),
        onAction = {},
        onNavigateBack = {},
        searchResultContent = {

        }
    )
}



