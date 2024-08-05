package com.masterplus.animals.features.savepoints.presentation.show_savepoints

import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestinationType
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint

data class ShowSavePointsState(
    val isLoading: Boolean = false,
    val savePoints: List<SavePoint> = emptyList(),
    private val selectedSavePoint: SavePoint? = null,
    val dropdownItems: List<IMenuItemEnum> = emptyList(),
    val selectedDropdownItem: IMenuItemEnum? = null,
    val showDropdownMenu: Boolean = false,
    val dialogEvent: ShowSavePointsDialogEvent? = null,
    val message: UiText? = null,
    val showImage: Boolean = true
){
    val currentSelectedSavePoint: SavePoint? get() =
        savePoints.find { it.id == selectedSavePoint?.id }
}
