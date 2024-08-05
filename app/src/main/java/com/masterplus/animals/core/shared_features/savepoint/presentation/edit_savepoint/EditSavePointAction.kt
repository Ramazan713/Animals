package com.masterplus.animals.core.shared_features.savepoint.presentation.edit_savepoint

import com.masterplus.animals.core.shared_features.savepoint.domain.models.EditSavePointLoadParam
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import kotlinx.datetime.LocalDateTime

sealed interface EditSavePointAction{

    data class LoadData(val loadPram: EditSavePointLoadParam): EditSavePointAction

    data object RequestAddNewSavePoint: EditSavePointAction

    data class Delete(val savePoint: SavePoint): EditSavePointAction

    data class EditTitle(val title: String, val savePoint: SavePoint): EditSavePointAction

    data class Select(val savePoint: SavePoint): EditSavePointAction

    data class AddSavePoint(
        val title: String,
        val posIndex: Int,
        val currentDateTime: LocalDateTime
    ): EditSavePointAction

    data class OverrideSavePoint(val pos: Int): EditSavePointAction

    data class ShowDialog(val dialogEvent: EditSavePointDialogEvent?): EditSavePointAction

    data object ClearMessage: EditSavePointAction
}
