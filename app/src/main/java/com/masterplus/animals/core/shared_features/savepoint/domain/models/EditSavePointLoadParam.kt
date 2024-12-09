package com.masterplus.animals.core.shared_features.savepoint.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination

data class EditSavePointLoadParam(
    val destinationTypeId: Int,
    val destinationId: Int?,
    val saveKey: String? = null,
    val kingdomType: KingdomType,
    val contentType: SavePointContentType = SavePointContentType.Content,
    val filteredDestinationTypeIds: List<Int>? = listOf(destinationTypeId)
){
    val destination = SavePointDestination.from(
        destinationId = destinationId,
        destinationTypeId = destinationTypeId,
        saveKey = saveKey,
        kingdomType = kingdomType
    )
}
