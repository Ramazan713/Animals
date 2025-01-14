package com.masterplus.animals.core.shared_features.savepoint.domain.models

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePointDestinationTypeId
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination

data class EditSavePointLoadParam(
    val destinationTypeId: Int,
    val destinationId: Int?,
    val kingdomType: KingdomType,
    val contentType: SavePointContentType = SavePointContentType.Content,
    val saveKey: String? = null
){
    val destination = SavePointDestination.from(
        destinationId = destinationId,
        destinationTypeId = destinationTypeId,
        saveKey = saveKey,
        kingdomType = kingdomType
    )

    companion object {
        fun fromCategory(
            categoryType: CategoryType,
            categoryItemId: Int?,
            kingdomType: KingdomType,
            returnAll: Boolean,
            contentType: SavePointContentType = SavePointContentType.Content,
            saveKey: String? = null,
        ): EditSavePointLoadParam{
            return EditSavePointLoadParam(
                destinationTypeId = categoryType.toSavePointDestinationTypeId(categoryItemId , returnAll = returnAll),
                destinationId = categoryItemId,
                kingdomType = kingdomType,
                contentType = contentType,
                saveKey = saveKey
            )
        }
    }
}
