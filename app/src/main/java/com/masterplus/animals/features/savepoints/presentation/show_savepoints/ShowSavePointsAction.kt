package com.masterplus.animals.features.savepoints.presentation.show_savepoints

import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint

sealed interface ShowSavePointsAction {

    data class Delete(val savePoint: SavePoint): ShowSavePointsAction

    data class EditTitle(val title: String, val savePoint: SavePoint): ShowSavePointsAction

    data class Select(val savePoint: SavePoint): ShowSavePointsAction

    data class ShowDialog(val dialogEvent: ShowSavePointsDialogEvent?): ShowSavePointsAction

    data class SelectDropdownMenuItem(val item: IMenuItemEnum): ShowSavePointsAction

    data object ClearMessage: ShowSavePointsAction
}