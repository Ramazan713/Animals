@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.masterplus.animals.features.species_detail.presentation


import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.presentation.components.image.DefaultImage
import com.masterplus.animals.core.presentation.components.image.TransitionImage
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.dialogs.ShowImageDia
import com.masterplus.animals.core.presentation.transition.TransitionImageKey
import com.masterplus.animals.core.presentation.transition.TransitionImageType
import com.masterplus.animals.core.presentation.transition.animateEnterExitForTransition
import com.masterplus.animals.core.presentation.transition.renderInSharedTransitionScopeOverlayDefault
import com.masterplus.animals.core.presentation.transition.sharedBoundsText
import com.masterplus.animals.core.presentation.utils.EventHandler
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.features.species_detail.domain.enums.SpeciesInfoPageEnum
import com.masterplus.animals.features.species_detail.presentation.components.TitleContentInfo
import com.masterplus.animals.features.species_detail.presentation.components.TitleSectionRow
import com.masterplus.animals.features.species_detail.presentation.mapper.toFeatureSection2
import com.masterplus.animals.features.species_detail.presentation.mapper.toFeatureSection3
import com.masterplus.animals.features.species_detail.presentation.mapper.toScientificNomenclatureSection
import com.masterplus.animals.features.species_detail.presentation.mapper.toTitleSections
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpeciesDetailPageRoot(
    onNavigateBack: () -> Unit,
    viewModel: SpeciesDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SpeciesDetailPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun SpeciesDetailPage(
    state: SpeciesDetailState,
    onAction: (SpeciesDetailAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val horizontalPagePadding = 8.dp

    val pagerState = rememberPagerState(
        initialPage = state.selectedPage.ordinal
    ) {
        SpeciesInfoPageEnum.entries.size
    }

    EventHandler(event = state.selectedPage) { pageEnum ->
        pagerState.animateScrollToPage(pageEnum.ordinal)
    }

    Scaffold { paddings ->
        val species = state.speciesDetail?.species

        SharedLoadingPageContent(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
            isLoading = state.isLoading,
            isEmptyResult = species == null,
        ) {
            if(species == null) return@SharedLoadingPageContent
            Column(
                modifier = Modifier
                    .padding(paddings)
                    .padding(horizontal = horizontalPagePadding)
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
                        onAction(SpeciesDetailAction.ShowDialog(SpeciesDetailDialogEvent.ShowImages(
                            images = state.speciesDetail.detail.images.map { it.image },
                            index = it
                        )))
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .renderInSharedTransitionScopeOverlayDefault()
                        .animateEnterExitForTransition(offsetY = { it })
                ) {
                    SegmentedButtonInPage(
                        state = state,
                        onAction = onAction,

                    )
                }

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) { pageIndex ->
                    when(pageIndex){
                        SpeciesInfoPageEnum.Info.ordinal ->  {
                            InfoPageContent(
                                modifier = Modifier.padding(vertical = 4.dp),
                                state = state,
                                species = species,
                                onShowImageClick = { image ->
                                    onAction(SpeciesDetailAction.ShowDialog(SpeciesDetailDialogEvent.ShowImages(
                                        images = listOf(image),
                                    )))
                                }
                            )
                        }
                        SpeciesInfoPageEnum.Features.ordinal ->  {
                            FeaturePageContent(
                                state = state
                            )
                        }
                    }
                }
            }

        }
    }

    val closeDialog = remember { {
        onAction(SpeciesDetailAction.ShowDialog(dialogEvent = null))
    } }

    state.dialogEvent?.let { dialogEvent->
        when(dialogEvent){
            is SpeciesDetailDialogEvent.ShowImages -> {
                ShowImageDia(
                    imageDataList = dialogEvent.images,
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
    state: SpeciesDetailState,
    onNavigateBack: () -> Unit,
    onShowImageClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val images = state.speciesDetail?.detail?.images ?: emptyList()
    val carouselState = rememberCarouselState {
        images.size
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

        if(images.isEmpty()){
            DefaultImage(
                imageData = "",
            )
        }else{
            val preferredItemWidth = 250.dp
            val minSmallItemWidth = preferredItemWidth * 0.8f
            val maxSmallItemWidth = preferredItemWidth * 1.5f
            HorizontalMultiBrowseCarousel(
                state = carouselState,
                preferredItemWidth = preferredItemWidth,
                minSmallItemWidth = minSmallItemWidth,
                maxSmallItemWidth = maxSmallItemWidth,
                itemSpacing = 4.dp,
                contentPadding = PaddingValues(horizontal = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) { index ->
                TransitionImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clickable {
                            onShowImageClick(index)
                        }
                    ,
                    image = images[index].image,
                    shape = RoundedCornerShape(16.dp),
                    transitionKey = state.speciesDetail?.images?.get(index)?.let {
                        TransitionImageKey(
                            id = it.speciesId,
                            extra = it.image.id?.toString(),
                            imageType = TransitionImageType.SpeciesImages
                        )
                    }
                )
            }
        }
    }
}


@Composable
private fun SegmentedButtonInPage(
    state: SpeciesDetailState,
    onAction: (SpeciesDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        SpeciesInfoPageEnum.entries.forEachIndexed { index, pageEnum ->
            SegmentedButton(
                selected = state.selectedPage == pageEnum,
                onClick = { onAction(SpeciesDetailAction.ChangePage(pageEnum)) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = SpeciesInfoPageEnum.entries.size)
            ) {
                Text(text = pageEnum.title)
            }
        }
    }
}


@Composable
private fun InfoPageContent(
    state: SpeciesDetailState,
    species: SpeciesModel,
    onShowImageClick: (ImageWithMetadata) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = species.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.sharedBoundsText(
                contentStateKey = species.name
            )
        )
        Text(
            text = species.scientificName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.sharedBoundsText(
                contentStateKey = species.scientificName
            )
        )

        Text(
            modifier = Modifier
                .sharedBoundsText(
                    contentStateKey = species.introduction
                )
                .padding(vertical = 16.dp)
                .fillMaxWidth()
            ,
            text = species.introduction,
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
    state: SpeciesDetailState,
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
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
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

        Spacer(Modifier.padding(bottom = 12.dp))
    }
}


@Preview(showBackground = true)
@Composable
private fun SpeciesDetailPagePreview() {
    SpeciesDetailPage(
        state = SpeciesDetailState(
            speciesDetail = SampleDatas.animalDetail,
            selectedPage = SpeciesInfoPageEnum.Info,
            titleSectionModels = SampleDatas.animal.toTitleSections(images = listOf(SampleDatas.imageWithMetadata, SampleDatas.imageWithMetadata)),
            scientificNomenclatureSection = SampleDatas.animalDetail.toScientificNomenclatureSection(),
            featureSection2 = SampleDatas.animalDetail.toFeatureSection2(),
            featureSection3 = SampleDatas.animal.toFeatureSection3()
        ),
        onAction = {},
        onNavigateBack = {}
    )
}














