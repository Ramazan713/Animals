package com.masterplus.animals.features.search.presentation.category_search


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.animals.R
import com.masterplus.animals.core.extentions.clearFocusOnTap
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.dialogs.LoadingDialog
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdMobResultHandler
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.core.shared_features.ad.presentation.AdUiResult
import com.masterplus.animals.core.shared_features.ad.presentation.dialogs.ShowAdRequiredDia
import com.masterplus.animals.features.search.domain.enums.SearchType
import com.masterplus.animals.features.search.presentation.components.HistoryItem
import com.masterplus.animals.features.search.presentation.components.SearchField


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
        label = state.adLabel
    ) { adResult ->
        when(adResult){
            is AdUiResult.OnShowingRewardSuccess -> {
                onAction(CategorySearchAction.AdShowedSuccess)
            }
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

            GetSearchFilters(
                state = state,
                onAction = onAction
            )

            AnimatedVisibility(
                visible = state.query.isBlank()
            ) {
                HistoryLazyColumn(
                    contentPaddings = contentPadding,
                    modifier = Modifier.weight(1f),
                    state = state,
                    onAction = onAction
                )
            }

            AnimatedVisibility(
                visible = state.query.isNotBlank()
            ) {
                searchResultContent(contentPadding)
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
fun GetSearchFilters(
    state: CategorySearchState,
    onAction: (CategorySearchAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchType.entries.forEach { searchType ->
            FilterChip(
                selected = state.searchType == searchType,
                onClick = { onAction(CategorySearchAction.SelectSearchType(searchType)) },
                label = { Text(searchType.title.asString()) },
                leadingIcon = { searchType.iconInfo?.imageVector?.let { Icon(it, contentDescription = null) } }
            )
        }
        Spacer(Modifier.weight(1f))
        AnimatedVisibility(
            state.searchType.isServer
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(state.remainingSearchableCount.toString())
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
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

@Composable
private fun HistoryLazyColumn(
    state: CategorySearchState,
    onAction: (CategorySearchAction) -> Unit,
    contentPaddings: PaddingValues,
    modifier: Modifier = Modifier
) {
    SharedLoadingPageContent(
        isLoading = state.historyLoading,
        isEmptyResult = state.histories.isEmpty(),
        emptyMessage = "No history found"
    ) {
        LazyColumn(
            contentPadding = contentPaddings,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
        ) {
            items(
                state.histories,
                key = { it.id ?: 0}
            ) { history ->
                HistoryItem(
                    history = history,
                    onClick = {
                        onAction(CategorySearchAction.SearchQuery(history.content))
                    },
                    onDeleteClick = {
                        onAction(CategorySearchAction.DeleteHistory(history))
                    }
                )
            }
        }
    }
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



