package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination

data class AutoSavePointConfig(
    val destination: SavePointDestination,
    val contentType: SavePointContentType
)
