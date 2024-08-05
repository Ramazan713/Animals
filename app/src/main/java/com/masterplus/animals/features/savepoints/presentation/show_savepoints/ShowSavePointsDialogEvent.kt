package com.masterplus.animals.features.savepoints.presentation.show_savepoints

import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint

sealed interface ShowSavePointsDialogEvent {

    data class AskDelete(val savePoint: SavePoint): ShowSavePointsDialogEvent

    data class EditTitle(val savePoint: SavePoint): ShowSavePointsDialogEvent
}