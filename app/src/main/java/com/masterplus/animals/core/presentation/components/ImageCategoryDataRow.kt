package com.masterplus.animals.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.extentions.isAnyLoading
import com.masterplus.animals.core.extentions.isEmptyResult
import com.masterplus.animals.core.presentation.components.image.ImageWithTitle
import com.masterplus.animals.core.presentation.components.loading.SharedCircularProgress
import com.masterplus.animals.core.presentation.utils.SampleDatas


@Composable
fun ImageCategoryDataRow(
    title: String,
    pagingItems: LazyPagingItems<CategoryData>,
    modifier: Modifier = Modifier,
    showMore: Boolean = false,
    showMoreItem: Boolean = showMore,
    onClickMore: (() -> Unit)? = null,
    onClickItem: (CategoryData) -> Unit,
    imageSize: DpSize = DpSize(150.dp, 180.dp),
    contentPaddings: PaddingValues = PaddingValues(),
    useTransition: Boolean = false,
    emptyContent: @Composable (() -> Unit)? = null,
){
    val isLoading = pagingItems.isAnyLoading()
    ImageCategoryDataRow(
        title = title,
        modifier = modifier,
        showMore = showMore,
        onClickMore = onClickMore,
        contentPaddings = contentPaddings,
        isLoading = isLoading
    ){ showMoreBtn ->
        val showEmptyContent = !isLoading && pagingItems.isEmptyResult()
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            DefaultAnimatedVisibility(showEmptyContent) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(3f),
                    contentAlignment = Alignment.Center
                ) {
                    emptyContent?.invoke()
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = contentPaddings
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id },
                    contentType = pagingItems.itemContentType {  }
                ){ index ->
                    val item = pagingItems[index]
                    if(item != null){
                        ImageWithTitle(
                            modifier = Modifier
                                .animateItem(),
                            model = item,
                            size = imageSize,
                            useTransition = useTransition,
                            onClick = {
                                onClickItem(item)
                            }
                        )
                    }else{
                        CategoryItemShipper()
                    }
                }

                item {
                    DefaultAnimatedVisibility(
                        !showEmptyContent && showMoreItem
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .height(imageSize.height)
                        ) {
                            showMoreBtn()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ImageCategoryDataRow(
    title: String,
    items: List<CategoryData>,
    modifier: Modifier = Modifier,
    showMore: Boolean = false,
    onClickMore: (() -> Unit)? = null,
    onClickItem: (CategoryData) -> Unit,
    imageSize: DpSize = DpSize(150.dp, 180.dp),
    contentPaddings: PaddingValues = PaddingValues(),
    useTransition: Boolean = false,
    isLoading: Boolean = false,
    emptyContent: @Composable (() -> Unit)? = null,
) {
    ImageCategoryDataRow(
        title = title,
        modifier = modifier,
        showMore = showMore,
        onClickMore = onClickMore,
        contentPaddings = contentPaddings,
        isLoading = isLoading
    ){ showMoreBtn ->
        val showEmptyContent = !isLoading && items.isEmpty()
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            DefaultAnimatedVisibility(showEmptyContent) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(3f),
                    contentAlignment = Alignment.Center
                ) {
                    emptyContent?.invoke()
                }
            }

            LazyRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = contentPaddings
            ) {
                this.items(
                    items = items,
                    key = { it.id ?: it.title }
                ){ item ->
                    ImageWithTitle(
                        modifier = Modifier.animateItem(),
                        model = item,
                        size = imageSize,
                        useTransition = useTransition,
                        onClick = {
                            onClickItem(item)
                        }
                    )
                }

                item {
                    DefaultAnimatedVisibility(
                        !showEmptyContent
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.height(imageSize.height)
                        ) {
                            showMoreBtn()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageCategoryDataRow(
    title: String,
    modifier: Modifier = Modifier,
    showMore: Boolean = false,
    onClickMore: (() -> Unit)? = null,
    contentPaddings: PaddingValues = PaddingValues(),
    isLoading: Boolean = false,
    content: @Composable ( showMoreBtn: @Composable () -> Unit ) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(contentPaddings)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(4.dp)
            )
            DefaultAnimatedVisibility(showMore) {
                Spacer(modifier = Modifier.width(12.dp))
                TextButton(
                    onClick = { onClickMore?.invoke() }
                ) {
                    Text(text = "daha fazlası")
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if(isLoading){
                SharedCircularProgress(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(enabled = false, onClick = {})
                        .zIndex(2f)
                )
            }
            content {
                if(showMore){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        TextButton(
                            onClick = { onClickMore?.invoke() }
                        ) {
                            Text(text = "Tümünü Göster")
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ImageCategoryRowPreview() {
    LazyColumn {
        item {
            ImageCategoryDataRow(
                title = "Sınıflar",
                items = listOf(SampleDatas.categoryData),
                useTransition = true,
                showMore = true,
                isLoading = false,
                onClickItem = {},
                emptyContent = {
                    TextButton(
                        onClick = {}
                    ) {
                        Text("Hello There")
                    }
                }
            )
        }
    }
}