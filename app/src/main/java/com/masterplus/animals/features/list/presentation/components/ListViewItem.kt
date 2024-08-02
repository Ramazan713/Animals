package com.masterplus.animals.features.list.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.shared_features.list.domain.models.ListView


@ExperimentalMaterial3Api
@Composable
fun ListViewItem(
    listView: ListView,
    onClick: ()->Unit,
    trailingItem: @Composable() () -> Unit,
    modifier: Modifier = Modifier
){
    val shape = MaterialTheme.shapes.medium

    val imageVector = if(listView.isRemovable) Icons.AutoMirrored.Filled.LibraryBooks else
        Icons.Default.Favorite

    val tintColor = if(listView.isRemovable) LocalContentColor.current else
        MaterialTheme.colorScheme.error

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.secondaryContainer, shape)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .clickable {
                onClick()
            }
            .padding(vertical = 6.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = imageVector,
            tint = tintColor,
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                listView.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                listView.itemCounts.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        trailingItem()
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListViewItemPreview() {
    ListViewItem(
        listView = SampleDatas.listView,
        onClick = {},
        trailingItem = {}
    )
}