package com.masterplus.animals.features.kingdom.presentation


import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.BuildConfig
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.presentation.components.DefaultTopBar
import com.masterplus.animals.core.presentation.components.ImageCategoryDataRow
import com.masterplus.animals.core.presentation.components.image.ImageWithTitle
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.defaults.SettingTopBarMenuEnum
import com.masterplus.animals.core.presentation.models.CategoryDataRowModel
import com.masterplus.animals.core.presentation.transition.animateEnterExitForTransition
import com.masterplus.animals.core.presentation.transition.renderInSharedTransitionScopeOverlayDefault
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.savepoint.presentation.components.SavePointItem
import com.masterplus.animals.core.shared_features.savepoint.presentation.components.SavePointItemDefaults
import com.masterplus.animals.features.kingdom.presentation.navigation.ItemId
import org.koin.compose.koinInject

@Composable
fun KingdomPageRoot(
    viewModel: KingdomBaseViewModel,
    serverReadCounter: ServerReadCounter = koinInject(),
    onNavigateToCategoryListWithDetail: (CategoryType, ItemId) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val contentCounter by serverReadCounter.contentCountersFlow.collectAsStateWithLifecycle(0)
    val categoryCounter by serverReadCounter.categoryCountersFlow.collectAsStateWithLifecycle(0)

    ShowLifecycleToastMessage(state.message) {
        viewModel.onAction(KingdomAction.ClearMessage)
    }
    KingdomPage(
        state = state,
        onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail,
        onNavigateToCategoryList = onNavigateToCategoryList,
        onNavigateToSpeciesList = onNavigateToSpeciesList,
        onNavigateToShowSavePoints = onNavigateToShowSavePoints,
        onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
        onNavigateToSettings = onNavigateToSettings,
        onAction = viewModel::onAction,
        onGetDebugAnalyticsValues = { Pair(contentCounter, categoryCounter)}
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun KingdomPage(
    state: KingdomState,
    onGetDebugAnalyticsValues: () -> Pair<Int, Int>,
    onAction: (KingdomAction) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, ItemId) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val contentPaddings = PaddingValues(horizontal = 12.dp)
    Scaffold(
        topBar = {
            DefaultTopBar(
                title = state.title.asString(),
                menuItems = SettingTopBarMenuEnum.entries,
                onMenuItemClick = { menuItem ->
                    when(menuItem){
                        SettingTopBarMenuEnum.Settings -> onNavigateToSettings()
                    }
                },
                modifier = Modifier
                    .renderInSharedTransitionScopeOverlayDefault()
                    .animateEnterExitForTransition()
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

                if(BuildConfig.DEBUG){
                    item {
                        val pairData = onGetDebugAnalyticsValues()
                        Column(
                            modifier = Modifier.padding(contentPaddings),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Content: ${pairData.first}")
                            Text("Category: ${pairData.second}")
                        }
                    }
                }

                if(state.dailySpecies.isLoading || state.dailySpecies.categoryDataList.isNotEmpty()){
                    item {
                        DailySpeciesSection(
                            modifier = Modifier.animateItem(),
                            state = state,
                            contentPaddings = contentPaddings,
                            onNavigateToSpeciesDetail = onNavigateToSpeciesDetail
                        )
                    }
                }

                if(state.savePoints.isNotEmpty() || state.isSavePointLoading){
                    item {
                        SavePointSection(
                            modifier = Modifier.animateItem(),
                            state = state,
                            contentPaddings = contentPaddings,
                            onNavigateToSpeciesList = onNavigateToSpeciesList,
                            onNavigateToShowSavePoints = onNavigateToShowSavePoints
                        )
                    }
                }


                item {
                    ImageCategoryDataRow(
                        modifier = Modifier.animateItem(),
                        contentPaddings = contentPaddings,
                        title = "Yaşam Alanları",
                        items = state.habitats.categoryDataList,
                        showMore = state.habitats.showMore,
                        isLoading = state.habitats.isLoading,
                        useTransition = false,
                        onClickItem = { item ->
                            onNavigateToSpeciesList(CategoryType.Habitat, item.id, 0)
                        },
                        onClickMore = {
                            onNavigateToCategoryList(CategoryType.Habitat)
                        },
                        emptyContent = {
                            TryAgainButton(onClick = {
                                onAction(KingdomAction.RetryCategory(CategoryType.Habitat))
                            })
                        }
                    )
                }

                item {
                    ImageCategoryDataRow(
                        modifier = Modifier.animateItem(),
                        contentPaddings = contentPaddings,
                        title = "Sınıflar",
                        items = state.classes.categoryDataList,
                        showMore = state.classes.showMore,
                        isLoading = state.classes.isLoading,
                        useTransition = true,
                        onClickMore = {
                            onNavigateToCategoryList(CategoryType.Class)
                        },
                        onClickItem = { item ->
                            onNavigateToCategoryListWithDetail(CategoryType.Class, item.id ?: 0)
                        },
                        emptyContent = {
                            TryAgainButton(onClick = {
                                onAction(KingdomAction.RetryCategory(CategoryType.Class))
                            })
                        }
                    )
                }

                item {
                    ImageCategoryDataRow(
                        modifier = Modifier.animateItem(),
                        contentPaddings = contentPaddings,
                        title = "Takımlar",
                        items = state.orders.categoryDataList,
                        showMore = state.orders.showMore,
                        isLoading = state.orders.isLoading,
                        useTransition = true,
                        onClickMore = {
                            onNavigateToCategoryList(CategoryType.Order)
                        },
                        onClickItem = { item ->
                            onNavigateToCategoryListWithDetail(CategoryType.Order, item.id ?: 0)
                        },
                        emptyContent = {
                            TryAgainButton(onClick = {
                                onAction(KingdomAction.RetryCategory(CategoryType.Order))
                            })
                        }
                    )
                }

                item {
                    ImageCategoryDataRow(
                        modifier = Modifier.animateItem(),
                        contentPaddings = contentPaddings,
                        title = "Familyalar",
                        items = state.families.categoryDataList,
                        showMore = state.families.showMore,
                        isLoading = state.families.isLoading,
                        useTransition = true,
                        onClickMore = {
                            onNavigateToCategoryList(CategoryType.Family)
                        },
                        onClickItem = { item ->
                            onNavigateToSpeciesList(CategoryType.Family, item.id, 0)
                        },
                        emptyContent = {
                            TryAgainButton(onClick = {
                                onAction(KingdomAction.RetryCategory(CategoryType.Family))
                            })
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailySpeciesSection(
    state: KingdomState,
    contentPaddings: PaddingValues,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryDataList = state.dailySpecies.categoryDataList
    ImageCategoryDataRow(
        modifier = modifier
            .requiredHeight(250.dp),
        title = "Günün ${if(state.kingdomType.isAnimals) "Hayvanları" else "Bitkileri"} ",
        contentPaddings = contentPaddings,
        isLoading = state.dailySpecies.isLoading,
    ) {
        if(categoryDataList.isNotEmpty()){
            HorizontalUncontainedCarousel(
                state = rememberCarouselState {
                    state.dailySpecies.categoryDataList.size
                },
                itemSpacing = 4.dp,
                itemWidth = 250.dp,
                contentPadding = contentPaddings
            ) { index ->
                val speciesData = categoryDataList[index]
                ImageWithTitle(
                    model = speciesData,
                    onClick = {
                        onNavigateToSpeciesDetail(speciesData.id)
                    },
                    size = DpSize(250.dp, 250.dp),
                    shape = RoundedCornerShape(16.dp),
                    useTransition = false
                )
            }
        }
    }
}


@Composable
private fun TryAgainButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .requiredHeight(100.dp),
        contentAlignment = Alignment.Center
    ) {
        TextButton(
            onClick = onClick,
        ) {
            Text("Tekrar Dene")
        }
    }

}

@Composable
private fun SavePointSection(
    state: KingdomState,
    contentPaddings: PaddingValues,
    onNavigateToSpeciesList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    modifier: Modifier = Modifier
) {
    val maxSavePointsSize = 5
    val savePointsSize = state.savePoints.size

    val savePointsLimited by remember {
        derivedStateOf {
            state.savePoints.subList(0, minOf(maxSavePointsSize,savePointsSize))
        }
    }

    ImageCategoryDataRow(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Max),
        contentPaddings = contentPaddings,
        title = "Kaldıgın yerden devam et",
        onClickMore = onNavigateToShowSavePoints,
        showMore = savePointsSize > maxSavePointsSize,
        isLoading = state.isSavePointLoading
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
                            savePoint.orderKey
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
    KingdomPage(
        state = KingdomState(
            isLoading = false,
            isSavePointLoading = true,
            habitats = CategoryDataRowModel(isLoading = true),
            dailySpecies = CategoryDataRowModel(isLoading = true),
            classes = CategoryDataRowModel(isLoading = true),
//            savePoints = listOf(SampleDatas.generateSavePoint(), SampleDatas.generateSavePoint(id = 2).copy(title = "Title"))
        ),
        onNavigateToCategoryListWithDetail = { _, _ ->},
        onNavigateToCategoryList = {},
        onNavigateToSpeciesList = { _, _, _ -> },
        onNavigateToShowSavePoints = {},
        onNavigateToSpeciesDetail = {},
        onNavigateToSettings = {},
        onAction = {},
        onGetDebugAnalyticsValues = {Pair(5,2)}
    )
}