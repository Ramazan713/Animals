package com.masterplus.animals.core.shared_features.savepoint.presentation.edit_savepoint

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.savepoint.domain.models.EditSavePointLoadParam
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint

data class EditSavePointState(
    val loadParam: EditSavePointLoadParam? = null,
    val savePoints: List<SavePoint> = emptyList(),
    private val selectedSavePoint: SavePoint? = null,
    val dialogEvent: EditSavePointDialogEvent? = null,
    val message: UiText? = null,
    val showImage: Boolean = true
){
    val currentSelectedSavePoint: SavePoint? get() =
        savePoints.find { it.id == selectedSavePoint?.id }
}
