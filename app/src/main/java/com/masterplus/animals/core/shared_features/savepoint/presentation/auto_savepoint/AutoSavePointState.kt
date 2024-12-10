package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

data class AutoSavePointState(
    val loadingSavePointPos: Boolean = false,
    val initPos: Int = 0,
    val uiEvent: AutoSavePointEvent? = null
)
