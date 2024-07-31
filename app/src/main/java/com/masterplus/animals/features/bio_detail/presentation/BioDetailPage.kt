package com.masterplus.animals.features.bio_detail.presentation


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.presentation.components.DefaultImage
import com.masterplus.animals.core.presentation.dialogs.ShowImageDia
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.features.bio_detail.domain.enums.BioInfoPageEnum
import com.masterplus.animals.features.bio_detail.presentation.components.TitleContentInfo
import com.masterplus.animals.features.bio_detail.presentation.components.TitleSectionRow
import com.masterplus.animals.features.bio_detail.presentation.mapper.toFeatureSection2
import com.masterplus.animals.features.bio_detail.presentation.mapper.toFeatureSection3
import com.masterplus.animals.features.bio_detail.presentation.mapper.toScientificNomenclatureSection
import com.masterplus.animals.features.bio_detail.presentation.mapper.toTitleSections
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@Composable
fun BioDetailPageRoot(
    onNavigateBack: () -> Unit,
    viewModel: BioDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BioDetailPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun BioDetailPage(
    state: BioDetailState,
    onAction: (BioDetailAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val horizontalPagePadding = 8.dp
    val lifecycleOwner = LocalLifecycleOwner.current

    val pagerState = rememberPagerState(
        initialPage = state.selectedPage.ordinal
    ) {
        BioInfoPageEnum.entries.size
    }
    LaunchedEffect(state.selectedPage, lifecycleOwner.lifecycle) {
        snapshotFlow { state.selectedPage }
            .distinctUntilChanged()
            .flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { pageEnum ->
                pagerState.animateScrollToPage(pageEnum.ordinal)
            }
    }


    Scaffold { paddings ->
        val animal = state.animal
        Box(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if(state.isLoading){
                CircularProgressIndicator()
            }
            else if(animal == null){
                Text(text = "Herhangi bir veri bulunamadı")
            }else{
                Column(
                    modifier = Modifier
                        .padding(paddings)
                        .padding(horizontal = horizontalPagePadding)
                        .padding(bottom = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        ,
                ) {
                    TopBarImage(
                        state = state,
                        modifier = Modifier
                            .fillMaxWidth()
                            .layout { measurable, constraints ->
                                val noPaddingConstraints = constraints.copy(
                                    maxWidth = constraints.maxWidth + (horizontalPagePadding * 2).roundToPx()
                                )
                                val placeable = measurable.measure(noPaddingConstraints)
                                layout(placeable.width, placeable.height) {
                                    placeable.place(x = -0.dp.roundToPx(), y = 0)
                                }
                            },
                        onNavigateBack = onNavigateBack,
                        onShowImageClick = {
                            onAction(BioDetailAction.ShowDialog(BioDetailDialogEvent.ShowImages(
                                imageUrls = state.animal?.imageUrls ?: emptyList(),
                                index = it
                            )))
                        }
                    )
                    SegmentedButtonInPage(
                        state = state,
                        onAction = onAction
                    )

                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = false
                    ) { pageIndex ->
                        when(pageIndex){
                            BioInfoPageEnum.Info.ordinal ->  {
                                InfoPageContent(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    state = state,
                                    animal = animal,
                                    onShowImageClick = { imageUrl ->
                                        onAction(BioDetailAction.ShowDialog(BioDetailDialogEvent.ShowImages(
                                            imageUrls = listOf(imageUrl),
                                        )))
                                    }
                                )
                            }
                            BioInfoPageEnum.Features.ordinal ->  {
                                FeaturePageContent(
                                    state = state
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    val closeDialog = remember { {
        onAction(BioDetailAction.ShowDialog(dialogEvent = null))
    } }

    state.dialogEvent?.let { dialogEvent->
        when(dialogEvent){
            is BioDetailDialogEvent.ShowImages -> {
                ShowImageDia(
                    imageDataList = dialogEvent.imageUrls,
                    onDismiss = closeDialog,
                    currentPageIndex = dialogEvent.index
                )
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarImage(
    state: BioDetailState,
    onNavigateBack: () -> Unit,
    onShowImageClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val imageUrls = state.animal?.imageUrls ?: emptyList()
    val carouselState = rememberCarouselState {
        imageUrls.size
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .height(300.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FilledTonalIconButton(
            modifier = Modifier
                .padding(top = 16.dp, start = 8.dp)
                .zIndex(1f)
                .align(Alignment.TopStart),
            onClick = onNavigateBack
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }

        if(imageUrls.isEmpty()){
            DefaultImage(
                imageData = "",
            )
        }else{
            HorizontalMultiBrowseCarousel(
                state = carouselState,
                preferredItemWidth = 350.dp,
                minSmallItemWidth = 200.dp,
                maxSmallItemWidth = 600.dp,
                itemSpacing = 4.dp,
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) { index ->
                DefaultImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            onShowImageClick(index)
                        }
                    ,
                    imageData = imageUrls[index],
                )
            }
        }
    }
}


@Composable
private fun SegmentedButtonInPage(
    state: BioDetailState,
    onAction: (BioDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        BioInfoPageEnum.entries.forEachIndexed { index, pageEnum ->
            SegmentedButton(
                selected = state.selectedPage == pageEnum,
                onClick = { onAction(BioDetailAction.ChangePage(pageEnum)) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = BioInfoPageEnum.entries.size)
            ) {
                Text(text = pageEnum.title)
            }
        }
    }
}


@Composable
private fun InfoPageContent(
    state: BioDetailState,
    animal: Animal,
    onShowImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = animal.name,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = animal.scientificName,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            text = animal.introduction,
            style = MaterialTheme.typography.bodyMedium
        )

        state.titleSectionModels.forEach { titleSectionModel ->
            TitleSectionRow(
                modifier = Modifier
                    .padding(bottom = 12.dp),
                titleSectionModel = titleSectionModel,
                onImageClick = onShowImageClick
            )
        }
    }
}


@Composable
private fun FeaturePageContent(
    state: BioDetailState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Bilimsel Adlandırma",
            style = MaterialTheme.typography.labelLarge
        )
        state.scientificNomenclatureSection.chunked(2).forEach { titleContentsRow ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                titleContentsRow.forEach { titleContent ->
                    TitleContentInfo(
                        modifier = Modifier.weight(1f),
                        titleContent = titleContent
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp)
        )
        state.featureSection2.chunked(2).forEach { titleContentsRow ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                titleContentsRow.forEach { titleContent ->
                    TitleContentInfo(
                        modifier = Modifier.weight(1f),
                        titleContent = titleContent
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp)
        )
        state.featureSection3.forEach { titleContent ->
            TitleContentInfo(
                titleContent = titleContent
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BioDetailPagePreview() {
    BioDetailPage(
        state = BioDetailState(
            animalDetail = SampleDatas.animalDetail,
            selectedPage = BioInfoPageEnum.Info,
            titleSectionModels = SampleDatas.animal.toTitleSections(),
            scientificNomenclatureSection = SampleDatas.animalDetail.toScientificNomenclatureSection(),
            featureSection2 = SampleDatas.animalDetail.toFeatureSection2(),
            featureSection3 = SampleDatas.animal.toFeatureSection3()
        ),
        onAction = {},
        onNavigateBack = {}
    )
}














