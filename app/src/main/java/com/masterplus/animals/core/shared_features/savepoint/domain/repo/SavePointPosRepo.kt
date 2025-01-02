package com.masterplus.animals.core.shared_features.savepoint.domain.repo

import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination

interface SavePointPosRepo {

    suspend fun getItemPos(
        orderKey: Int,
        destination: SavePointDestination,
        contentType: SavePointContentType
    ): Int?
}