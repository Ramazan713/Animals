package com.masterplus.animals.features.savepoints.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.features.savepoints.presentation.show_savepoints.ShowSavePointsPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class ShowSavePointsRoute(
    val contentType: SavePointContentType,
    val kingdomType: KingdomType,
    val filteredDestinationTypeIds: List<Int>?
)

fun NavController.navigateToShowSavePoints(
    contentType: SavePointContentType = SavePointContentType.Content,
    filteredDestinationTypeIds: List<Int>?,
    kingdomType: KingdomType
) {
    navigate(
        ShowSavePointsRoute(
            contentType = contentType,
            kingdomType = kingdomType,
            filteredDestinationTypeIds = filteredDestinationTypeIds
        )
    )
}



fun NavGraphBuilder.showSavePoints(
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, KingdomType, Int) -> Unit,
) {
    composable<ShowSavePointsRoute> {
        ShowSavePointsPageRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToSpeciesList = { savepoint ->
                onNavigateToSpeciesList(
                    savepoint.destination.toCategoryType() ?: CategoryType.Order,
                    savepoint.destination.destinationId,
                    KingdomType.Animals,
                    savepoint.orderKey,
                )
            }
        )
    }
}