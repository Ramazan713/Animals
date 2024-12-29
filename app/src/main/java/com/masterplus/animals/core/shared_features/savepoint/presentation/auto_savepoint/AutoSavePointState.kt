package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

data class AutoSavePointState(
    val loadingSavePointPos: Boolean = false,
    val initPos: Int = 0,
    val isInitLoaded: Boolean = false,
    val uiEvent: AutoSavePointEvent? = null,
    val config: AutoSavePointConfig? = null,
    val dialogEvent: AutoSavePointDialogEvent? = null,
    val navToPosRequest: AutoSavePointRequestPos? = null
)


data class AutoSavePointRequestPos(
    val itemId: Int,
    val label: String
)