package com.masterplus.animals.features.animal.presentation


import AnimalViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.presentation.components.ImageCategoryRow
import com.masterplus.animals.features.animal.presentation.navigation.ItemId
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnimalPageRoot(
    viewModel: AnimalViewModel = koinViewModel(),
    onNavigateToCategoryListWithDetail: (CategoryType, ItemId) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToBioList: (CategoryType, Int?) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AnimalPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail,
        onNavigateToCategoryList = onNavigateToCategoryList,
        onNavigateToBioList = onNavigateToBioList
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalPage(
    state: AnimalState,
    onAction: (AnimalAction) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, ItemId) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToBioList: (CategoryType, Int?) -> Unit,
) {
    val contentPaddings = PaddingValues(horizontal = 12.dp)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(text = "Hayvanlar Alemi")
                }
            )
        }
    ) { paddings ->
        Box(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if(state.isLoading){
                CircularProgressIndicator()
            }else{
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        ImageCategoryRow(
                            contentPaddings = contentPaddings,
                            title = "Yaşam Alanları",
                            items = state.habitats.imageWithTitleModels,
                            showMore = state.habitats.showMore,
                            onClickItem = { item ->
                                onNavigateToBioList(CategoryType.Habitat, item.id)
                            }
                        )
                    }

                    item {
                        ImageCategoryRow(
                            contentPaddings = contentPaddings,
                            title = "Sınıflar",
                            items = state.classes.imageWithTitleModels,
                            showMore = state.classes.showMore,
                            onClickMore = {
                                onNavigateToCategoryList(CategoryType.Class)
                            },
                            onClickItem = { item ->
                                onNavigateToCategoryListWithDetail(CategoryType.Class, item.id ?: 0)
                            }
                        )
                    }

                    item {
                        ImageCategoryRow(
                            contentPaddings = contentPaddings,
                            title = "Takımlar",
                            items = state.orders.imageWithTitleModels,
                            showMore = state.orders.showMore,
                            onClickMore = {
                                onNavigateToCategoryList(CategoryType.Order)
                            },
                            onClickItem = { item ->
                                onNavigateToCategoryListWithDetail(CategoryType.Order, item.id ?: 0)
                            }
                        )
                    }

                    item {
                        ImageCategoryRow(
                            contentPaddings = contentPaddings,
                            title = "Familyalar",
                            items = state.families.imageWithTitleModels,
                            showMore = state.families.showMore,
                            onClickMore = {
                                onNavigateToCategoryList(CategoryType.Family)
                            },
                            onClickItem = { item ->
                                onNavigateToBioList(CategoryType.Family, item.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimalPagePreview() {
    AnimalPage(
        state = AnimalState(
            isLoading = true
        ),
        onAction = {},
        onNavigateToCategoryListWithDetail = { x, y ->},
        onNavigateToCategoryList = {},
        onNavigateToBioList = {x,y -> }
    )
}