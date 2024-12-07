package com.masterplus.animals.features.category_list.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.presentation.components.ImageWithTitle
import com.masterplus.animals.core.presentation.components.NavigationBackIcon
import com.masterplus.animals.core.presentation.components.SharedCircularProgress
import com.masterplus.animals.core.presentation.components.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListPage(
    state: CategoryState,
    pagingItems: LazyPagingItems<ImageWithTitleModel>,
    onAction: (CategoryAction) -> Unit,
    onNavigateBack: () -> Unit,
    onAllItemClick: () -> Unit,
    onItemClick: (ImageWithTitleModel) -> Unit,
    onNavigateToCategorySearch: () -> Unit
) {
    val topBarScrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val imageUrl by remember(state, pagingItems.itemCount) {
        derivedStateOf {
            when {
                state.isLoading -> null
                state.parentImageData != null && pagingItems.itemCount > 0 -> state.parentImageData
                pagingItems.itemCount > 0 -> R.drawable.animals_plants
                else -> null
            }
        }
    }

    Scaffold(
        topBar = {
            GetTopBar(
                topBarScrollBehaviour = topBarScrollBehaviour,
                onNavigateBack = onNavigateBack,
                title = state.title,
                subTitle = state.subTitle,
                onNavigateToCategorySearch = onNavigateToCategorySearch
            )
        }
    ) { paddings->

        SharedLoadingPageContent(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize()
                .nestedScroll(topBarScrollBehaviour.nestedScrollConnection),
            isLoading = pagingItems.loadState.refresh is LoadState.Loading,
        ) {
            LazyColumn(
                modifier = Modifier
                    .matchParentSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {

                item {
                    AnimatedVisibility(
                        enter = fadeIn() + expandIn(),
                        visible = imageUrl != null
                    ) {
                        ImageWithTitle(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            title = "${if(state.kingdomType.isAnimals) "Hayvanlar" else "Bitkiler"} Listesi",
                            imageData = imageUrl ?: "",
                            onClick = onAllItemClick
                        )
                    }
                }

                item {
                    Text(
                        text = state.collectionName,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                items(
                    count = pagingItems.itemCount,
                    key = { pagingItems[it]?.id ?: it }
                ){ index ->
                    val item = pagingItems[index]
                    if(item != null){
                        ImageWithTitle(
                            modifier = Modifier.fillMaxWidth(),
                            model = item,
                            order = index + 1,
                            onClick = {
                                onItemClick(item)
                            }
                        )
                    }
                }
                if(pagingItems.loadState.append is LoadState.Loading){
                    item {
                        SharedCircularProgress(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GetTopBar(
    topBarScrollBehaviour: TopAppBarScrollBehavior,
    onNavigateBack: () -> Unit,
    onNavigateToCategorySearch: () -> Unit,
    title: String,
    subTitle: String?
) {
    LargeTopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )
                if(!subTitle.isNullOrBlank() && topBarScrollBehaviour.state.collapsedFraction <= 0.4f ){
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },
        navigationIcon = {
            NavigationBackIcon(onNavigateBack = onNavigateBack)
        },
        scrollBehavior = topBarScrollBehaviour,
        actions = {
            IconButton(onClick = onNavigateToCategorySearch) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
private fun CategoryListPagePreview1() {
    CategoryListPage(
        state = CategoryState(kingdomType = KingdomType.Animals),
        onAction = {},
        pagingItems = getPreviewLazyPagingData(
            items = listOf(
                SampleDatas.imageWithTitleModel1
            ),
//            sourceLoadStates = previewPagingLoadStates(refresh = LoadState.Loading)
        ),
        onNavigateBack = {

        },
        onItemClick = {},
        onAllItemClick = {},
        onNavigateToCategorySearch = {}
    )
}

//@Preview(showBackground = true)
//@Composable
//fun CategoryListPagePreview2() {
//    CategoryListPage(
//        state = CategoryListState(),
//        onAction = {},
//        items = getPreviewLazyPagingData(
//            items = listOf(
//                SampleDatas.imageWithTitleModel1
//            ),
//            sourceLoadStates = previewPagingLoadStates(
//                append = LoadState.Loading
//            )
//        )
//    )
//}