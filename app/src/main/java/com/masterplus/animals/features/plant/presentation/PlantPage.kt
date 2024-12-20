package com.masterplus.animals.features.plant.presentation


import PlantViewModel
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.presentation.components.ImageCategoryDataRow
import com.masterplus.animals.core.presentation.components.image.ImageWithTitle
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.selections.CustomDropdownBarMenu
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toCategoryType
import com.masterplus.animals.core.shared_features.savepoint.presentation.components.SavePointItem
import com.masterplus.animals.core.shared_features.savepoint.presentation.components.SavePointItemDefaults
import com.masterplus.animals.features.animal.presentation.navigation.ItemId
import com.masterplus.animals.features.plant.domain.enums.PlantTopBarMenu
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlantPageRoot(
    viewModel: PlantViewModel = koinViewModel(),
    onNavigateToCategoryListWithDetail: (CategoryType, Int) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlantPage(
        state = state,
        onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail,
        onNavigateToCategoryList = onNavigateToCategoryList,
        onNavigateToSpeciesList = onNavigateToSpeciesList,
        onNavigateToShowSavePoints = onNavigateToShowSavePoints,
        onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
        onNavigateToSettings = onNavigateToSettings
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantPage(
    state: PlantState,
    onNavigateToCategoryListWithDetail: (CategoryType, ItemId) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val contentPaddings = PaddingValues(horizontal = 12.dp)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(text = stringResource(id = R.string.plant_kingdom))
                },
                actions = {
                    CustomDropdownBarMenu(
                        items = PlantTopBarMenu.entries,
                        onItemChange = { menuItem ->
                            when(menuItem){
                                PlantTopBarMenu.Settings -> onNavigateToSettings()
                            }
                        }
                    )
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
                    DailyPlantsSection(
                        state = state,
                        contentPaddings = contentPaddings,
                        onNavigateToSpeciesDetail = onNavigateToSpeciesDetail
                    )
                }

                if(state.savePoints.isNotEmpty()){
                    item {
                        SavePointSection(
                            state = state,
                            contentPaddings = contentPaddings,
                            onNavigateToSpeciesList = onNavigateToSpeciesList,
                            onNavigateToShowSavePoints = onNavigateToShowSavePoints
                        )
                    }
                }


                item {
                    ImageCategoryDataRow(
                        contentPaddings = contentPaddings,
                        title = "Yaşam Alanları",
                        items = state.habitats.categoryDataList,
                        showMore = state.habitats.showMore,
                        useTransition = true,
                        onClickItem = { item ->
                            onNavigateToSpeciesList(CategoryType.Habitat, item.id, 0)
                        },
                        onClickMore = {
                            onNavigateToCategoryList(CategoryType.Habitat)
                        },
                    )
                }

                item {
                    ImageCategoryDataRow(
                        contentPaddings = contentPaddings,
                        title = "Sınıflar",
                        items = state.classes.categoryDataList,
                        showMore = state.classes.showMore,
                        useTransition = true,
                        onClickMore = {
                            onNavigateToCategoryList(CategoryType.Class)
                        },
                        onClickItem = { item ->
                            onNavigateToCategoryListWithDetail(CategoryType.Class, item.id ?: 0)
                        }
                    )
                }

                item {
                    ImageCategoryDataRow(
                        contentPaddings = contentPaddings,
                        title = "Takımlar",
                        items = state.orders.categoryDataList,
                        showMore = state.orders.showMore,
                        useTransition = true,
                        onClickMore = {
                            onNavigateToCategoryList(CategoryType.Order)
                        },
                        onClickItem = { item ->
                            onNavigateToCategoryListWithDetail(CategoryType.Order, item.id ?: 0)
                        }
                    )
                }

                item {
                    ImageCategoryDataRow(
                        contentPaddings = contentPaddings,
                        title = "Familyalar",
                        items = state.families.categoryDataList,
                        showMore = state.families.showMore,
                        useTransition = true,
                        onClickMore = {
                            onNavigateToCategoryList(CategoryType.Family)
                        },
                        onClickItem = { item ->
                            onNavigateToSpeciesList(CategoryType.Family, item.id, 0)
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyPlantsSection(
    state: PlantState,
    contentPaddings: PaddingValues,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dailyPlantTitleModels = state.dailyPlants.imageWithTitleModels
    if(dailyPlantTitleModels.isNotEmpty()){
        ImageCategoryDataRow(
            modifier = modifier,
            title = "Günün Bitkileri",
            contentPaddings = contentPaddings
        ){
            HorizontalUncontainedCarousel(
                state = rememberCarouselState {
                    dailyPlantTitleModels.size
                },
                itemSpacing = 4.dp,
                itemWidth = 250.dp,
                contentPadding = contentPaddings
            ) { index ->
                val plantData = dailyPlantTitleModels[index]
                ImageWithTitle(
                    image = plantData.image,
                    onClick = {
                        onNavigateToSpeciesDetail(plantData.id ?: return@ImageWithTitle)
                    },
                    title = plantData.title,
                    subTitle = plantData.subTitle,
                    size = DpSize(250.dp,250.dp),
                    shape = RoundedCornerShape(16.dp),
                    transitionKey = null
                )
            }
        }
    }
}

@Composable
private fun SavePointSection(
    state: PlantState,
    contentPaddings: PaddingValues,
    onNavigateToSpeciesList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    modifier: Modifier = Modifier
) {
    val maxSavePointsSize = 5
    val savePointsSize = state.savePoints.size
    val savePointsLimited = state.savePoints.subList(0, minOf(maxSavePointsSize,savePointsSize))

    ImageCategoryDataRow(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Max),
        contentPaddings = contentPaddings,
        title = "Kaldıgın yerden devam et",
        onClickMore = onNavigateToShowSavePoints,
        showMore = savePointsSize > maxSavePointsSize,
    ){ showMoreBtn ->
        Row (
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Max)
                .horizontalScroll(rememberScrollState())
                .padding(contentPaddings)
            ,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            savePointsLimited.forEach { savePoint ->
                SavePointItem(
                    modifier = Modifier
                        .widthIn(min = 170.dp, max = 200.dp)
                        .fillMaxHeight(),
                    itemDefaults = SavePointItemDefaults(
                        showAsRow = false,
                        compactContent = true,
                        titleMaxLineLimit = 1,
                        titleStyle = MaterialTheme.typography.titleSmall
                    ),
                    savePoint = savePoint,
                    onClick = {
                        onNavigateToSpeciesList(
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
fun PlantPagePreview() {
    PlantPage(
        state = PlantState(
            isLoading = false,
            savePoints = listOf(SampleDatas.generateSavePoint(), SampleDatas.generateSavePoint(id = 2).copy(title = "Title"))
        ),
        onNavigateToCategoryListWithDetail = { _, _ ->},
        onNavigateToCategoryList = {},
        onNavigateToSpeciesList = { _, _, _ -> },
        onNavigateToShowSavePoints = {},
        onNavigateToSpeciesDetail = {},
        onNavigateToSettings = {}
    )
}