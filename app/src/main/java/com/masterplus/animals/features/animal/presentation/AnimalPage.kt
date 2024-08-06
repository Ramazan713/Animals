package com.masterplus.animals.features.animal.presentation


import AnimalViewModel
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.presentation.components.ImageCategoryRow
import com.masterplus.animals.core.presentation.components.ImageWithTitle
import com.masterplus.animals.core.presentation.components.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toCategoryType
import com.masterplus.animals.core.shared_features.savepoint.presentation.components.SavePointItem
import com.masterplus.animals.features.animal.presentation.navigation.ItemId
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnimalPageRoot(
    viewModel: AnimalViewModel = koinViewModel(),
    onNavigateToCategoryListWithDetail: (CategoryType, ItemId) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToBioList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    onNavigateToBioDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AnimalPage(
        state = state,
        onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail,
        onNavigateToCategoryList = onNavigateToCategoryList,
        onNavigateToBioList = onNavigateToBioList,
        onNavigateToShowSavePoints = onNavigateToShowSavePoints,
        onNavigateToBioDetail = onNavigateToBioDetail
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalPage(
    state: AnimalState,
    onNavigateToCategoryListWithDetail: (CategoryType, ItemId) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToBioList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    onNavigateToBioDetail: (Int) -> Unit
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
        SharedLoadingPageContent(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
            isLoading = state.isLoading,
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                item {
                    DailyAnimalsSection(
                        state = state,
                        contentPaddings = contentPaddings,
                        onNavigateToBioDetail = onNavigateToBioDetail
                    )
                }

                if(state.savePoints.isNotEmpty()){
                    item {
                        SavePointSection(
                            state = state,
                            contentPaddings = contentPaddings,
                            onNavigateToBioList = onNavigateToBioList,
                            onNavigateToShowSavePoints = onNavigateToShowSavePoints
                        )
                    }
                }


                item {
                    ImageCategoryRow(
                        contentPaddings = contentPaddings,
                        title = "Yaşam Alanları",
                        items = state.habitats.imageWithTitleModels,
                        showMore = state.habitats.showMore,
                        onClickItem = { item ->
                            onNavigateToBioList(CategoryType.Habitat, item.id, 0)
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
                            onNavigateToBioList(CategoryType.Family, item.id, 0)
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyAnimalsSection(
    state: AnimalState,
    contentPaddings: PaddingValues,
    onNavigateToBioDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dailyAnimalTitleModels = state.dailyAnimals.imageWithTitleModels
    if(dailyAnimalTitleModels.isNotEmpty()){
        ImageCategoryRow(
            modifier = modifier,
            title = "Günün Hayvanları",
            contentPaddings = contentPaddings
        ){
            HorizontalUncontainedCarousel(
                state = rememberCarouselState {
                    dailyAnimalTitleModels.size
                },
                itemSpacing = 4.dp,
                itemWidth = 250.dp,
                contentPadding = contentPaddings
            ) { index ->
                val animalData = dailyAnimalTitleModels[index]
                ImageWithTitle(
                    imageData = animalData.imageUrl,
                    onClick = {
                        onNavigateToBioDetail(animalData.id ?: return@ImageWithTitle)
                    },
                    title = animalData.title,
                    subTitle = animalData.subTitle,
                    size = DpSize(250.dp,250.dp),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SavePointSection(
    state: AnimalState,
    contentPaddings: PaddingValues,
    onNavigateToBioList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    modifier: Modifier = Modifier
) {
    ImageCategoryRow(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Max),
        contentPaddings = contentPaddings,
        title = "Kaldıgın yerden devam et",
        onClickMore = onNavigateToShowSavePoints,
        showMore = true,
    ){ showMoreBtn ->
        Row (
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Max)
                .horizontalScroll(rememberScrollState())
                .padding(contentPaddings)
            ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            state.savePoints.forEach { savePoint ->
                SavePointItem(
                    modifier = Modifier
                        .widthIn(min = 170.dp, max = 200.dp)
                        .fillMaxHeight(),
                    showAsRow = false,
                    savePoint = savePoint,
                    onClick = {
                        onNavigateToBioList(
                            savePoint.destination.toCategoryType() ?: CategoryType.Order,
                            savePoint.destination.destinationId,
                            savePoint.itemPosIndex
                        )
                    }
                )
            }
            showMoreBtn()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnimalPagePreview() {
    AnimalPage(
        state = AnimalState(
            isLoading = false,
            savePoints = listOf(SampleDatas.generateSavePoint(), SampleDatas.generateSavePoint(id = 2).copy(title = "Title"))
        ),
        onNavigateToCategoryListWithDetail = { _, _ ->},
        onNavigateToCategoryList = {},
        onNavigateToBioList = { _, _, _ -> },
        onNavigateToShowSavePoints = {},
        onNavigateToBioDetail = {}
    )
}