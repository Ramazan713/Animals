package com.masterplus.animals.features.search.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masterplus.animals.core.presentation.components.DefaultAnimatedVisibility
import com.masterplus.animals.features.search.domain.enums.SearchType

@Composable
fun SearchFilterRow(
    selectedSearchType: SearchType,
    onSelectSearchType: (SearchType) -> Unit,
    onRemainingSearchableCount: () -> Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchType.entries.forEach { searchType ->
            FilterChip(
                selected = selectedSearchType == searchType,
                onClick = { onSelectSearchType(searchType) },
                label = { Text(searchType.title.asString()) },
                leadingIcon = { searchType.iconInfo?.imageVector?.let { Icon(it, contentDescription = null) } }
            )
        }
        Spacer(Modifier.weight(1f))
        DefaultAnimatedVisibility(
            selectedSearchType.isServer
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(onRemainingSearchableCount().toString())
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
    }
}