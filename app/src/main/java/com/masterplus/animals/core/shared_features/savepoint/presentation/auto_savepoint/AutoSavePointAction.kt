package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination

sealed interface AutoSavePointAction {

    data class Init(
        val destination: SavePointDestination,
        val contentType: SavePointContentType,
    ): AutoSavePointAction

    data class UpsertSavePoint(
        val destination: SavePointDestination,
        val contentType: SavePointContentType,
        val orderKey: Int
    ): AutoSavePointAction

    data class LoadSavePoint(
        val destination: SavePointDestination,
        val contentType: SavePointContentType,
        val initOrderKey: Int
    ): AutoSavePointAction

    data object ClearUiEvent: AutoSavePointAction

    data class RequestNavigateToPosByOrderKey(
        val orderKey: Int,
    ): AutoSavePointAction

    data object ShowAd: AutoSavePointAction

    data class SuccessShowAd(
        val contentType: ContentType,
    ): AutoSavePointAction

    data class ShowDialog(val dialogEvent: AutoSavePointDialogEvent? = null): AutoSavePointAction
}