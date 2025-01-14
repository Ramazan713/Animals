@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.masterplus.animals.features.species_detail.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material.icons.outlined.LibraryAddCheck
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.presentation.components.DefaultTopBar
import com.masterplus.animals.core.presentation.components.image.DefaultImage
import com.masterplus.animals.core.presentation.components.image.TransitionImage
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.dialogs.ShowImageDia
import com.masterplus.animals.core.presentation.transition.TransitionImageKey
import com.masterplus.animals.core.presentation.transition.TransitionImageType
import com.masterplus.animals.core.presentation.transition.TransitionTextKey
import com.masterplus.animals.core.presentation.transition.TransitionTextType
import com.masterplus.animals.core.presentation.transition.animateEnterExitForTransition
import com.masterplus.animals.core.presentation.transition.renderInSharedTransitionScopeOverlayDefault
import com.masterplus.animals.core.presentation.transition.sharedBoundsText
import com.masterplus.animals.core.presentation.utils.EventHandler
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListAction
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListHandler
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListState
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListViewModel
import com.masterplus.animals.core.shared_features.list.presentation.select_list.ShowSelectListBottom
import com.masterplus.animals.features.species_detail.domain.enums.SpeciesInfoPageEnum
import com.masterplus.animals.features.species_detail.presentation.components.TitleContentInfo
import com.masterplus.animals.features.species_detail.presentation.components.TitleSectionRow
import com.masterplus.animals.features.species_detail.presentation.mapper.toFeatureSection2
import com.masterplus.animals.features.species_detail.presentation.mapper.toFeatureSection3
import com.masterplus.animals.features.species_detail.presentation.mapper.toScientificNomenclatureSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpeciesDetailPageRoot(
    onNavigateBack: () -> Unit,
    viewModel: SpeciesDetailViewModel = koinViewModel(),
    addSpeciesToListViewModel: AddSpeciesToListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val addSpeciesState by addSpeciesToListViewModel.state.collectAsStateWithLifecycle()

    ShowLifecycleToastMessage(state.message) {
        viewModel.onAction(SpeciesDetailAction.ClearMessage)
    }

    SpeciesDetailPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
        addSpeciesState = addSpeciesState,
        onAddSpeciesAction = addSpeciesToListViewModel::onAction,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SpeciesDetailPage(
    state: SpeciesDetailState,
    onAction: (SpeciesDetailAction) -> Unit,
    addSpeciesState: AddSpeciesToListState,
    onAddSpeciesAction: (AddSpeciesToListAction) -> Unit,
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

    Scaffold(
        topBar = {
            if(!state.isLoading && state.species == null){
                DefaultTopBar(title = "", onNavigateBack = onNavigateBack)
            }
        }
    ) { paddings ->
        val species = state.species

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
                            images = state.images.map { it.image },
                            index = it
                        )))
                    }
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .renderInSharedTransitionScopeOverlayDefault()
                        .animateEnterExitForTransition(offsetY = { it })
                        .padding(top = 10.dp, bottom = 8.dp)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SegmentedButtonInPage(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                    )
                }

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalAlignment = Alignment.Top
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
                                },
                                onAction = onAction,
                                onAddSpeciesAction = onAddSpeciesAction
                            )
                        }
                        SpeciesInfoPageEnum.Features.ordinal ->  {
                            FeaturePageContent(
                                state = state,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }

        }
    }


    AddSpeciesToListHandler(
        state = addSpeciesState,
        onAction = onAddSpeciesAction,
        listIdControl = state.listIdControl,
    )

    state.dialogEvent?.let { dialogEvent->
        val closeDialog = remember { {
            onAction(SpeciesDetailAction.ShowDialog(dialogEvent = null))
        } }

        when(dialogEvent){
            is SpeciesDetailDialogEvent.ShowImages -> {
                ShowImageDia(
                    imageDataList = dialogEvent.images,
                    onDismiss = closeDialog,
                    currentPageIndex = dialogEvent.index
                )
            }

            SpeciesDetailDialogEvent.ShowSelectListBottom -> {
                val species = state.species ?: return
                ShowSelectListBottom(
                    speciesId = species.id,
                    listIdControl = state.listIdControl,
                    title = stringResource(id = R.string.add_to_list),
                    onClosed = closeDialog
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
    val images = state.images
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
                    transitionKey = state.images[index].let {
                        TransitionImageKey(
                            id = it.speciesId,
                            extra = it.image.id?.toString(),
                            imageType = TransitionImageType.SpeciesImages,
                            kingdomType = state.species?.kingdomType ?: KingdomType.DEFAULT
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
private fun ListButtons(
    state: SpeciesDetailState,
    onAddSpeciesAction: (AddSpeciesToListAction) -> Unit,
    onAction: (SpeciesDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilledTonalButton(
            modifier = Modifier.animateContentSize(),
            onClick = {
                val speciesId = state.species?.id ?: return@FilledTonalButton
                if(state.isFavorited){
                    onAddSpeciesAction(AddSpeciesToListAction.AddOrAskFavorite(speciesId))
                }else{
                    onAddSpeciesAction(AddSpeciesToListAction.AddToFavorite(speciesId))
                }
            },
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = if(state.isFavorited) MaterialTheme.colorScheme.error else Color.Unspecified
            )
            AnimatedVisibility(visible = !state.isFavorited) {
                Text(text = "Favoriye Ekle", modifier = Modifier.padding(start = 8.dp),)
            }
        }

        FilledTonalButton(
            modifier = Modifier.animateContentSize(),
            onClick =  {
                onAction(SpeciesDetailAction.ShowDialog(SpeciesDetailDialogEvent.ShowSelectListBottom))
            },
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Icon(if(state.isListSelected)Icons.Default.LibraryAddCheck else Icons.Outlined.LibraryAddCheck, contentDescription = null)
            AnimatedVisibility(visible = !state.isListSelected) {
                Text(text = "Listeye Ekle", modifier = Modifier.padding(start = 8.dp),)
            }
        }
    }
}


@Composable
private fun InfoPageContent(
    state: SpeciesDetailState,
    species: SpeciesModel,
    onAddSpeciesAction: (AddSpeciesToListAction) -> Unit,
    onAction: (SpeciesDetailAction) -> Unit,
    onShowImageClick: (ImageWithMetadata) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {

        ListButtons(
            state = state,
            onAddSpeciesAction = onAddSpeciesAction,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp)
        )

        Text(
            text = species.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.sharedBoundsText(
                textKey = TransitionTextKey(
                    text = species.name,
                    textType = TransitionTextType.Title
                )
            )
        )
        Text(
            text = species.scientificName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.sharedBoundsText(
                textKey = TransitionTextKey(
                    text = species.scientificName,
                    textType = TransitionTextType.Content
                )
            )
        )

        Text(
            modifier = Modifier
                .sharedBoundsText(
                    textKey = TransitionTextKey(
                        text = species.introduction,
                        textType = TransitionTextType.Description
                    )
                )
                .padding(vertical = 16.dp)
                .fillMaxWidth()
            ,
            text = species.introduction,
            style = MaterialTheme.typography.bodyMedium
        )

        val titleSectionModels = state.titleSectionModels

        if(titleSectionModels.isEmpty()){
            DataNotLoaded(modifier = Modifier.padding(vertical = 8.dp))
        }
        titleSectionModels.forEach { titleSectionModel ->
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
    val scientificNomenclatureSection = state.scientificNomenclatureSection
    val featureSection2 = state.featureSection2
    val featureSection3 = state.featureSection3

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        if(scientificNomenclatureSection.isEmpty() && featureSection2.isEmpty() &&featureSection3.isEmpty()){
            DataNotLoaded(modifier = Modifier.padding(vertical = 8.dp))
            return@Column
        }

        Text(
            text = "Bilimsel Adlandırma",
            style = MaterialTheme.typography.labelLarge
        )
        scientificNomenclatureSection.chunked(2).forEach { titleContentsRow ->
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
        if(scientificNomenclatureSection.isNotEmpty()){
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        featureSection2.chunked(2).forEach { titleContentsRow ->
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

        if(featureSection2.isNotEmpty()){
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }

        featureSection3.forEach { titleContent ->
            TitleContentInfo(
                titleContent = titleContent
            )
        }

        Spacer(Modifier.padding(bottom = 12.dp))
    }
}


@Composable
private fun DataNotLoaded(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Veriler Yüklenemedi",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.W500
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun SpeciesDetailPagePreview() {
    SpeciesDetailPage(
        state = SpeciesDetailState(
            selectedPage = SpeciesInfoPageEnum.Info,
            isLoading = false,
            species = SampleDatas.species,
//            titleSectionModels = SampleDatas.animal.toTitleSections(images = listOf(SampleDatas.imageWithMetadata, SampleDatas.imageWithMetadata)),
            scientificNomenclatureSection = SampleDatas.animalDetail.toScientificNomenclatureSection(),
            featureSection2 = SampleDatas.animalDetail.toFeatureSection2(),
            featureSection3 = SampleDatas.animal.toFeatureSection3()
        ),
        onAction = {},
        onNavigateBack = {},
        addSpeciesState = AddSpeciesToListState(),
        onAddSpeciesAction = {}
    )
}














