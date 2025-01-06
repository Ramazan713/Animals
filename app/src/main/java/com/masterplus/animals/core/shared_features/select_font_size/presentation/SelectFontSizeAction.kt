package com.masterplus.animals.core.shared_features.select_font_size.presentation

sealed interface SelectFontSizeAction {

    data class ChangeSliderPos(val pos: Float): SelectFontSizeAction

    data object RestoreChanges: SelectFontSizeAction

    data object ApplyChanges: SelectFontSizeAction
}