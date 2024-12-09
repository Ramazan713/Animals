package com.masterplus.animals.core.shared_features.savepoint.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import kotlinx.datetime.LocalDateTime

data class SavePoint(
    val id: Int? = null,
    val title: String,
    val contentType: SavePointContentType,
    val destination: SavePointDestination,
    val kingdomType: KingdomType,
    val itemPosIndex: Int,
    val modifiedTime: LocalDateTime,
    val imageData: Any?,
    val imagePath: String?
)
