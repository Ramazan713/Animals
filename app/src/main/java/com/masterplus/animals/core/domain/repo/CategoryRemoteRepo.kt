package com.masterplus.animals.core.domain.repo

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult

interface CategoryRemoteRepo {

    suspend fun getClasses(
        kingdomType: KingdomType,
        limit: Int,
        phylumId: Int? = null,
        refresh: Boolean = false
    ): EmptyDefaultResult


    suspend fun getPhylums(
        kingdomType: KingdomType,
        limit: Int,
        startAfter: Int? = null,
        refresh: Boolean = false
    ): EmptyDefaultResult


    suspend fun getOrders(
        kingdomType: KingdomType,
        limit: Int,
        classId: Int? = null,
        startAfter: Int? = null,
        refresh: Boolean = false
    ): EmptyDefaultResult


    suspend fun getFamilies(
        kingdomType: KingdomType,
        limit: Int,
        orderId: Int? = null,
        startAfter: Int? = null,
        refresh: Boolean = false
    ): EmptyDefaultResult


    suspend fun getHabitats(
        kingdomType: KingdomType,
        limit: Int,
        startAfter: Int? = null,
        refresh: Boolean = false
    ): EmptyDefaultResult
}