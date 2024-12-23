package com.masterplus.animals.core.data.datasources

import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.database.entity_helper.ClassWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.FamilyWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.HabitatWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.OrderWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.PhylumWithImageEmbedded

interface CategoryRemoteSource {

    suspend fun getSpeciesByKingdom(
        kingdomType: KingdomType,
        limit: Int,
        startAfter: Int?
    ): DefaultResult<List<SpeciesDto>>

    suspend fun getSpeciesCategories(
        categoryType: CategoryType,
        itemId: Int,
        limit: Int,
        startAfter: Int?
    ): DefaultResult<List<SpeciesDto>>

    suspend fun getPhylums(
        kingdomType: KingdomType,
        limit: Int,
        startAfter: Int? = null
    ): DefaultResult<List<PhylumWithImageEmbedded>>

    suspend fun getClasses(
        kingdomType: KingdomType,
        limit: Int,
        phylumId: Int? = null,
        startAfter: Int? = null
    ): DefaultResult<List<ClassWithImageEmbedded>>

    suspend fun getOrders(
        kingdomType: KingdomType,
        limit: Int,
        classId: Int? = null,
        startAfter: Int? = null
    ): DefaultResult<List<OrderWithImageEmbedded>>


    suspend fun getFamilies(
        kingdomType: KingdomType,
        limit: Int,
        orderId: Int? = null,
        startAfter: Int? = null
    ): DefaultResult<List<FamilyWithImageEmbedded>>


    suspend fun getHabitats(
        kingdomType: KingdomType,
        limit: Int,
        startAfter: Int? = null
    ): DefaultResult<List<HabitatWithImageEmbedded>>

}