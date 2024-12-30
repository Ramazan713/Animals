package com.masterplus.animals.core.domain.repo

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult

interface CategoryRemoteRepo {

    suspend fun getClasses(
        kingdomType: KingdomType,
        limit: Int,
        phylumId: Int? = null,
        loadKey: Int? = null,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): EmptyDefaultResult


    suspend fun getPhylums(
        kingdomType: KingdomType,
        limit: Int,
        loadKey: Int? = null,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): EmptyDefaultResult


    suspend fun getOrders(
        kingdomType: KingdomType,
        limit: Int,
        classId: Int? = null,
        loadKey: Int? = null,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): EmptyDefaultResult


    suspend fun getFamilies(
        kingdomType: KingdomType,
        limit: Int,
        orderId: Int? = null,
        loadKey: Int? = null,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): EmptyDefaultResult


    suspend fun getHabitats(
        kingdomType: KingdomType,
        limit: Int,
        loadKey: Int? = null,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): EmptyDefaultResult
}