package com.masterplus.animals.features.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.features.search.domain.models.History

@Composable
fun SearchHistoryColumn(
    histories: List<History>,
    isLoading: Boolean,
    onHistoryClick: (History) -> Unit,
    onDeleteHistoryClick: (History) -> Unit,
    modifier: Modifier = Modifier,
    contentPaddings: PaddingValues = PaddingValues()
) {
    SharedLoadingPageContent(
        modifier = modifier,
        isLoading = isLoading,
        isEmptyResult = histories.isEmpty(),
        emptyMessage = "No history found"
    ) {
        LazyColumn(
            contentPadding = contentPaddings,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.matchParentSize()
        ) {
            items(
                histories,
                key = { it.id ?: 0}
            ) { history ->
                HistoryItem(
                    history = history,
                    onClick = { onHistoryClick(history) },
                    onDeleteClick = { onDeleteHistoryClick(history) }
                )
            }
        }
    }
}