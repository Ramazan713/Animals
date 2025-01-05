package com.masterplus.animals.core.data.datasources


import com.masterplus.animals.core.data.dtos.ClassDto
import com.masterplus.animals.core.data.dtos.FamilyDto
import com.masterplus.animals.core.data.dtos.HabitatCategoryDto
import com.masterplus.animals.core.data.dtos.OrderDto
import com.masterplus.animals.core.data.dtos.PhylumDto
import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
import com.masterplus.animals.core.domain.utils.DefaultResult

interface CategoryRemoteSource {

    suspend fun getSpeciesByKingdom(
        kingdomType: KingdomType,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): DefaultResult<List<SpeciesDto>>

    suspend fun getSpeciesCategories(
        categoryType: CategoryType,
        kingdomType: KingdomType,
        itemId: Int,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): DefaultResult<List<SpeciesDto>>

    suspend fun getSpecies(
        itemIds: List<Int>,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): DefaultResult<List<SpeciesDto>>

    suspend fun getPhylums(
        kingdomType: KingdomType,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): DefaultResult<List<PhylumDto>>

    suspend fun getPhylumById(
        itemId: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
    ): DefaultResult<PhylumDto?>


    suspend fun getClasses(
        kingdomType: KingdomType,
        phylumId: Int? = null,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): DefaultResult<List<ClassDto>>

    suspend fun getClassById(
        itemId: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
    ): DefaultResult<ClassDto?>

    suspend fun getOrders(
        kingdomType: KingdomType,
        classId: Int? = null,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): DefaultResult<List<OrderDto>>

    suspend fun getOrderById(
        itemId: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
    ): DefaultResult<OrderDto?>

    suspend fun getFamilies(
        kingdomType: KingdomType,
        orderId: Int? = null,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): DefaultResult<List<FamilyDto>>

    suspend fun getFamilyById(
        itemId: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT
    ): DefaultResult<FamilyDto?>


    suspend fun getHabitats(
        kingdomType: KingdomType,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): DefaultResult<List<HabitatCategoryDto>>

    suspend fun getHabitatById(
        itemId: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT
    ): DefaultResult<HabitatCategoryDto?>

    suspend fun getHabitatsByIds(
        itemIds: List<Int>,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType = RemoteSourceType.DEFAULT,
        loadType: RemoteLoadType = RemoteLoadType.APPEND
    ): DefaultResult<List<HabitatCategoryDto>>

}