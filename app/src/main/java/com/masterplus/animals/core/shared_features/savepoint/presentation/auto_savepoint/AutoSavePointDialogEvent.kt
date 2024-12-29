package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

sealed interface AutoSavePointDialogEvent {

    data object ShowAdRequired: AutoSavePointDialogEvent
}