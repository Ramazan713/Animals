package com.masterplus.animals.features.bio_list.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.animals.core.domain.models.AnimalData
import com.masterplus.animals.core.presentation.components.ImageWithTitle
import com.masterplus.animals.core.presentation.components.NavigationBackIcon
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.features.bio_list.presentation.components.BioCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun BioListPageRoot(
    viewModel: BioListViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToBioDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagingItems = viewModel.pagingItems.collectAsLazyPagingItems()

    BioListPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
        onNavigateToBioDetail = onNavigateToBioDetail,
        pagingItems = pagingItems
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BioListPage(
    pagingItems: LazyPagingItems<AnimalData>,
    state: BioListState,
    onAction: (BioListAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToBioDetail: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = state.title)
                },
                navigationIcon = {
                    NavigationBackIcon(onNavigateBack = onNavigateBack)
                }
            )
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            if(pagingItems.loadState.refresh is LoadState.Loading){
                CircularProgressIndicator()
            }else{
                LazyColumn(
                    modifier = Modifier
                        .matchParentSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(
                        count = pagingItems.itemCount,
                        key = { pagingItems[it]?.id ?: it }
                    ){ index ->
                        val item = pagingItems[index]
                        if(item != null){
                            BioCard(
                                animalData = item,
                                orderNum = index + 1,
                                onClick = {
                                    onNavigateToBioDetail(item.id ?: 0)
                                },
                                onFavoriteClick = { },
                                onUnFavoriteClick = { },
                                onMenuButtonClick = { },
                            )
                        }
                    }
                    if(pagingItems.loadState.append is LoadState.Loading){
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BioListPagePreview() {
    BioListPage(
        state = BioListState(),
        onAction = {},
        onNavigateBack = {},
        onNavigateToBioDetail = {},
        pagingItems = getPreviewLazyPagingData(items = listOf())
    )
}