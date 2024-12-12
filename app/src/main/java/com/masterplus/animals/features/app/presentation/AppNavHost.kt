package com.masterplus.animals.features.app.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.features.animal.presentation.navigation.AnimalRoute
import com.masterplus.animals.features.animal.presentation.navigation.animal
import com.masterplus.animals.features.category_list.presentation.navigation.categoryList
import com.masterplus.animals.features.category_list.presentation.navigation.categoryListWithDetail
import com.masterplus.animals.features.category_list.presentation.navigation.navigateToCategoryList
import com.masterplus.animals.features.category_list.presentation.navigation.navigateToCategoryListWithDetail
import com.masterplus.animals.features.list.presentation.archive_list.navigation.archiveList
import com.masterplus.animals.features.list.presentation.archive_list.navigation.navigateToArchiveList
import com.masterplus.animals.features.list.presentation.show_list.navigation.showList
import com.masterplus.animals.features.plant.presentation.navigation.plant
import com.masterplus.animals.features.savepoints.presentation.show_savepoints.navigation.navigateToShowSavePoints
import com.masterplus.animals.features.savepoints.presentation.show_savepoints.navigation.showSavePoints
import com.masterplus.animals.features.search.presentation.navigation.navigateToSearchCategory
import com.masterplus.animals.features.search.presentation.navigation.navigateToSearchSpecies
import com.masterplus.animals.features.search.presentation.navigation.searchCategory
import com.masterplus.animals.features.search.presentation.navigation.searchSpecies
import com.masterplus.animals.features.settings.presentation.navigation.linkAccounts
import com.masterplus.animals.features.settings.presentation.navigation.navigateToLinkAccounts
import com.masterplus.animals.features.settings.presentation.navigation.navigateToSavePointCategorySettings
import com.masterplus.animals.features.settings.presentation.navigation.navigateToSavePointSettings
import com.masterplus.animals.features.settings.presentation.navigation.navigateToSavePointSpeciesSettings
import com.masterplus.animals.features.settings.presentation.navigation.navigateToSettings
import com.masterplus.animals.features.settings.presentation.navigation.savePointCategorySettings
import com.masterplus.animals.features.settings.presentation.navigation.savePointSettings
import com.masterplus.animals.features.settings.presentation.navigation.savePointSpeciesSettings
import com.masterplus.animals.features.settings.presentation.navigation.settings
import com.masterplus.animals.features.species_detail.presentation.navigation.navigateToSpeciesDetail
import com.masterplus.animals.features.species_detail.presentation.navigation.speciesDetail
import com.masterplus.animals.features.species_list.presentation.navigation.navigateToSpeciesList
import com.masterplus.animals.features.species_list.presentation.navigation.speciesList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun AppNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = AnimalRoute
    ){
        animal(
            onNavigateToCategoryList = { categoryType ->
                navHostController.navigateToCategoryList(categoryType, KingdomType.Animals)
            },
            onNavigateToCategoryListWithDetail = { categoryType, itemId ->
                navHostController.navigateToCategoryListWithDetail(categoryType, itemId, KingdomType.Animals)
            },
            onNavigateToSpeciesList = { categoryType, itemId, pos ->

                navHostController.navigateToSpeciesList(categoryType.catId, itemId, KingdomType.Animals, pos)
            },
            onNavigateToShowSavePoints = {
                navHostController.navigateToShowSavePoints(filteredDestinationTypeIds = null, kingdomType = KingdomType.Animals)
            },
            onNavigateToSpeciesDetail = { itemId ->
                navHostController.navigateToSpeciesDetail(itemId)
            },
            onNavigateToSettings = {
                navHostController.navigateToSettings()
            }
        )

        plant(
            onNavigateToCategoryList = { categoryType ->
                navHostController.navigateToCategoryList(categoryType, KingdomType.Plants)
            },
            onNavigateToCategoryListWithDetail = { categoryType, itemId ->
                navHostController.navigateToCategoryListWithDetail(categoryType, itemId, KingdomType.Plants)
            },
            onNavigateToSpeciesList = { categoryType, itemId, pos ->
                navHostController.navigateToSpeciesList(categoryType.catId, itemId, KingdomType.Plants, pos)
            },
            onNavigateToShowSavePoints = {
                navHostController.navigateToShowSavePoints(filteredDestinationTypeIds = null, kingdomType = KingdomType.Plants)
            },
            onNavigateToSpeciesDetail = { itemId ->
                navHostController.navigateToSpeciesDetail(itemId)
            },
            onNavigateToSettings = {
                navHostController.navigateToSettings()
            }
        )

        settings(
            onNavigateBack = {
                navHostController.navigateUp()
            },
            onNavigateToLinkedAccounts = {
                navHostController.navigateToLinkAccounts()
            },
            onNavigateToSavePointSettings = {
                navHostController.navigateToSavePointSettings()
            }
        )
        savePointSettings(onNavigateBack = { navHostController.navigateUp() })
        savePointSpeciesSettings(onNavigateBack = { navHostController.navigateUp() })
        savePointCategorySettings(onNavigateBack = { navHostController.navigateUp() })


        linkAccounts(
            onNavigateBack = {
                navHostController.navigateUp()
            }
        )

        showList(
            onNavigateToDetailList = {listId ->
                navHostController.navigateToSpeciesList(CategoryType.List.catId, listId)
            },
            onNavigateToArchive = {
                navHostController.navigateToArchiveList()
            }
        )

        archiveList(
            onNavigateBack = {
                navHostController.navigateUp()
            },
            onNavigateToDetailList = { listId ->
                navHostController.navigateToSpeciesList(CategoryType.List.catId, listId)
            }
        )

        categoryList(
            onNavigateBack = {
                navHostController.navigateUp()
            },
            onNavigateToSpeciesList = { categoryType, itemId, kingdomType ->
                navHostController.navigateToSpeciesList(categoryType.catId, itemId, kingdomType)
            },
            onNavigateToCategoryListWithDetail = { categoryType, itemId, kingdomType ->
                navHostController.navigateToCategoryListWithDetail(categoryType, itemId, kingdomType)
            },
            onNavigateToCategorySearch = { catType, contentType, kingdomType ->
                navHostController.navigateToSearchCategory(catType,contentType,null, kingdomType)
            },
            onNavigateToSavePointCategorySettings = {
                navHostController.navigateToSavePointCategorySettings()
            }
        )

        categoryListWithDetail(
            onNavigateBack = {
                navHostController.navigateUp()
            },
            onNavigateToSpeciesList = { categoryType, itemId, kingdomType ->
                navHostController.navigateToSpeciesList(categoryType.catId, itemId, kingdomType)
            },
            onNavigateToCategoryListWithDetail = { categoryType, itemId, kingdomType ->
                navHostController.navigateToCategoryListWithDetail(categoryType, itemId, kingdomType)
            },
            onNavigateToCategorySearch = { catType, contentType, itemId, kingdomType ->
                navHostController.navigateToSearchCategory(catType,contentType, itemId, kingdomType)
            },
            onNavigateToSavePointCategorySettings = {
                navHostController.navigateToSavePointCategorySettings()
            }
        )

        speciesList(
            onNavigateBack = {
                navHostController.navigateUp()
            },
            onNavigateToSpeciesDetail = { itemId ->
                navHostController.navigateToSpeciesDetail(itemId)
            },
            onNavigateToCategorySearch = {catType, contentType, itemId ->
                navHostController.navigateToSearchSpecies(catType,contentType, itemId)
            },
            onNavigateToSavePointSpeciesSettings = {
                navHostController.navigateToSavePointSpeciesSettings()
            }
        )

        speciesDetail(
            onNavigateBack = {
                navHostController.navigateUp()
            }
        )

        showSavePoints(
            onNavigateBack = {
                navHostController.navigateUp()
            },
            onNavigateToSpeciesList = { categoryType, itemId, kingdomType, pos ->
                navHostController.navigateToSpeciesList(categoryType.catId, itemId, kingdomType, pos)
            },
        )

        searchCategory(
            onNavigateBack = {
                navHostController.navigateUp()
            },
            onNavigateToSpeciesList = { categoryType, itemId, kingdomType ->
                navHostController.navigateToSpeciesList(categoryType.catId, itemId, kingdomType)
            },
            onNavigateToCategoryListWithDetail = { categoryType, itemId, kingdomType ->
                navHostController.navigateToCategoryListWithDetail(categoryType, itemId, kingdomType)
            },
        )

        searchSpecies(
            onNavigateBack = {
                navHostController.navigateUp()
            },
            onNavigateToSpeciesDetail = { itemId ->
                navHostController.navigateToSpeciesDetail(itemId)
            },
        )
    }
}