package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination

sealed interface AutoSavePointAction {
    data class UpsertSavePoint(
        val destination: SavePointDestination,
        val contentType: SavePointContentType,
        val itemPosIndex: Int
    ): AutoSavePointAction

    data class LoadSavePoint(
        val destination: SavePointDestination,
        val contentType: SavePointContentType,
        val initItemPos: Int
    ): AutoSavePointAction

    data object ClearUiEvent: AutoSavePointAction

    data class RequestNavigateToPosByItemId(
        val itemId: Int,
        val label: String
    ): AutoSavePointAction

    data object ShowAd: AutoSavePointAction

    data object SuccessShowAd: AutoSavePointAction

    data class ShowDialog(val dialogEvent: AutoSavePointDialogEvent? = null): AutoSavePointAction
}