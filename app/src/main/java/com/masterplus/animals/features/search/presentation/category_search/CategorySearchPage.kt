package com.masterplus.animals.features.search.presentation.category_search


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.masterplus.animals.R
import com.masterplus.animals.core.extentions.clearFocusOnTap
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.features.search.presentation.components.HistoryItem
import com.masterplus.animals.features.search.presentation.components.SearchField


@Composable
fun CategorySearchPage(
    state: CategorySearchState,
    onAction: (CategorySearchAction) -> Unit,
    onNavigateBack: () -> Unit,
    searchResultContent: @Composable (ColumnScope.(PaddingValues) -> Unit)
) {

    val contentPadding = PaddingValues(
        bottom = 16.dp,
        top = 16.dp
    )

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
            onAction(CategorySearchAction.InsertHistory(state.query))
        },
        onClear = {
            onAction(CategorySearchAction.InsertHistory(state.query))
            onAction(CategorySearchAction.SearchQuery(""))
        },
        placeholder = placeholder
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






