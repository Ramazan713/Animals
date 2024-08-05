package com.masterplus.animals.core.shared_features.savepoint.presentation.edit_savepoint

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import kotlinx.datetime.LocalDateTime

sealed interface EditSavePointDialogEvent {

    data class AskDelete(val savePoint: SavePoint): EditSavePointDialogEvent

    data class EditTitle(val savePoint: SavePoint): EditSavePointDialogEvent

    data class AddSavePointTitle(
        val suggestedTitle: UiText,
        val currentDateTime: LocalDateTime
    ): EditSavePointDialogEvent
}