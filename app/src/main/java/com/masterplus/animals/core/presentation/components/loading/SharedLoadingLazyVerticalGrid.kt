package com.masterplus.animals.core.presentation.components.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.masterplus.animals.R


@Composable
fun SharedLoadingLazyVerticalGrid(
    state: LazyGridState = rememberLazyGridState(),
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    verticalSpaceBy: Dp = 8.dp,
    horizontalSpaceBy: Dp = 8.dp,
    isEmptyResult: Boolean = false,
    emptyMessage: String = stringResource(id = R.string.not_fount_any_result),
    stickHeaderContent:  (LazyGridScope.() -> Unit)? = null,
    content:  LazyGridScope.() -> Unit,
) {
    val showEmptyResult by remember(isEmptyResult, isLoading) {
        derivedStateOf {
            !isLoading && isEmptyResult
        }
    }

    Box(
        modifier = modifier,
    ) {
        if (isLoading) {
            SharedCircularProgress(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f)
                    .clickable(enabled = false, onClick = {})
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 400.dp),
            state = state,
            verticalArrangement = Arrangement.spacedBy(verticalSpaceBy),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpaceBy),
            contentPadding = contentPadding
        ) {
            stickHeaderContent?.invoke(this)

            if (showEmptyResult) {
                item(
                    span = {
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxSize()
                            .zIndex(2f)
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            emptyMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            content()
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SharedPageContentPreview() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GetPreviewContent(title = "loading false", isLoading = false)
        }

        item {
            GetPreviewContent(title = "loading true",  isLoading =true)
        }

        item {
            GetPreviewContent(title = "loading false and isEmptyResult true", isLoading = false, isEmptyResult = true)
        }

        item {
            GetPreviewContent(title = "loading false and isEmptyResult false", isLoading = false, isEmptyResult = false)
        }
    }
}

@Composable
private fun GetPreviewContent(
    isLoading: Boolean,
    title: String,
    isEmptyResult: Boolean = false,
) {

    Column(
        modifier = Modifier.height(400.dp)
    ) {
        Text(text = title)
        SharedLoadingLazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),
            isLoading = isLoading,
            isEmptyResult = isEmptyResult,
            stickHeaderContent = {
                item {
                    Box(
                        modifier = Modifier.height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sticky World",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            content = {
                item {
                    Box(
                        modifier = Modifier.height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Hello world",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        )
    }

}