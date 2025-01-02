package com.masterplus.animals.core.shared_features.savepoint.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import kotlinx.datetime.LocalDateTime

data class SavePoint(
    val id: Int? = null,
    var title: String,
    val contentType: SavePointContentType,
    val destination: SavePointDestination,
    val kingdomType: KingdomType,
    val saveMode: SavePointSaveMode,
    var orderKey: Int,
    val modifiedTime: LocalDateTime,
    val image: ImageWithMetadata?
)
