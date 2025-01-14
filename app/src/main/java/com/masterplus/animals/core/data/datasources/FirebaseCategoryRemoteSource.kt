package com.masterplus.animals.core.data.datasources

import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.masterplus.animals.core.data.dtos.ClassDto
import com.masterplus.animals.core.data.dtos.FamilyDto
import com.masterplus.animals.core.data.dtos.HabitatCategoryDto
import com.masterplus.animals.core.data.dtos.OrderDto
import com.masterplus.animals.core.data.dtos.PhylumDto
import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.extensions.toFirebaseSource
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.enums.OrderDirection
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.ErrorText
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.safeCall
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import kotlinx.coroutines.tasks.await

class FirebaseCategoryRemoteSource(
    private val serverReadCounter: ServerReadCounter
): CategoryRemoteSource {

    override suspend fun getSpeciesByKingdom(
        kingdomType: KingdomType,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): DefaultResult<List<SpeciesDto>> {
        return execute(
            contentType = ContentType.Content,
            orderDirection = loadType.orderDirection,
            mapper = { items -> items.map { it.toObject<SpeciesDto>() } }
        )  {
            Firebase.firestore.collection("Species")
                .whereEqualTo("kingdom_id", kingdomType.kingdomId)
                .customBuild(loadKey = loadKey, loadType = loadType, limit = limit)
                .get(sourceType.toFirebaseSource())
                .await()
        }
    }

    override suspend fun getSpeciesCategories(
        categoryType: CategoryType,
        kingdomType: KingdomType,
        itemId: Int,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): DefaultResult<List<SpeciesDto>> {
        return execute(
            contentType = ContentType.Content,
            orderDirection = loadType.orderDirection,
            mapper = { items -> items.map { it.toObject<SpeciesDto>() } }
        )   {
            var query: Query = Firebase.firestore.collection("Species")
            query = when(categoryType){
                CategoryType.Class -> query.where(Filter.equalTo("class_id", itemId))
                CategoryType.Order -> query.where(Filter.equalTo("order_id", itemId))
                CategoryType.Family -> query.where(Filter.equalTo("family_id", itemId))
                CategoryType.Habitat -> {
                    query.where(Filter.arrayContains("habitats", itemId))
                        .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
                }
                else -> query
            }
            query
                .customBuild(loadKey = loadKey, loadType = loadType, limit = limit)
                .get(sourceType.toFirebaseSource())
                .await()
        }
    }

    override suspend fun getSpecies(
        itemIds: List<Int>,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType,
        orderByKey: String?,
        incrementCounter: Boolean
    ): DefaultResult<List<SpeciesDto>> {
        return execute(
            contentType = ContentType.Content,
            orderDirection = loadType.orderDirection,
            incrementCounter = incrementCounter,
            mapper = { items -> items.map { it.toObject<SpeciesDto>() } }
        )   {
            Firebase.firestore.collection("Species")
                .whereIn("id", itemIds)
                .customBuild(loadKey = loadKey, loadType = loadType, limit = limit, orderByKey = orderByKey)
                .get(sourceType.toFirebaseSource())
                .await()
        }
    }

    override suspend fun getPhylums(
        kingdomType: KingdomType,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType,
    ): DefaultResult<List<PhylumDto>> {
        return execute(
            contentType = ContentType.Category,
            orderDirection = loadType.orderDirection,
            mapper = { items -> items.map { it.toObject<PhylumDto>() } }
        ) {
            Firebase.firestore.collection("Phylums")
                .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
                .customBuild(loadKey = loadKey, loadType = loadType, limit = limit)
                .get(sourceType.toFirebaseSource())
                .await()
        }
    }

    override suspend fun getPhylums(
        itemIds: List<Int>,
        orderByKey: String?,
        incrementCounter: Boolean
    ): DefaultResult<List<PhylumDto>> {
        return execute(
            contentType = ContentType.Category,
            incrementCounter = incrementCounter,
            mapper = { items -> items.map { it.toObject<PhylumDto>() } }
        )   {
            Firebase.firestore.collection("Phylums")
                .whereIn("id", itemIds)
                .customBuild(limit = itemIds.size, orderByKey = orderByKey)
                .get()
                .await()
        }
    }

    override suspend fun getPhylumById(
        itemId: Int,
        sourceType: RemoteSourceType
    ): DefaultResult<PhylumDto?> {
        return execute2(
            contentType = ContentType.Category,
            mapper = { it.map { it.toObject<PhylumDto>() }.firstOrNull() }
        ) {
            Firebase.firestore.collection("Phylums")
                .where(Filter.equalTo("id",itemId))
                .get(sourceType.toFirebaseSource())
                .await()
        }
    }

    override suspend fun getClasses(
        kingdomType: KingdomType,
        phylumId: Int?,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): DefaultResult<List<ClassDto>> {
        return execute(
            contentType = ContentType.Category,
            orderDirection = loadType.orderDirection,
            mapper = { items -> items.map { it.toObject<ClassDto>()} }
        ) {
            var query = Firebase.firestore.collection("Classes")
                .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
            if(phylumId != null){
                query = query.where(Filter.equalTo("phylum_id",phylumId))
            }
            query
                .customBuild(loadKey = loadKey, loadType = loadType, limit = limit)
                .get(sourceType.toFirebaseSource())
                .await()

        }
    }

    override suspend fun getClasses(
        itemIds: List<Int>,
        orderByKey: String?,
        incrementCounter: Boolean
    ): DefaultResult<List<ClassDto>> {
        return execute(
            contentType = ContentType.Category,
            incrementCounter = incrementCounter,
            mapper = { items -> items.map { it.toObject<ClassDto>() } }
        )   {
            Firebase.firestore.collection("Classes")
                .whereIn("id", itemIds)
                .customBuild(limit = itemIds.size, orderByKey = orderByKey)
                .get()
                .await()
        }
    }

    override suspend fun getClassById(
        itemId: Int,
        sourceType: RemoteSourceType
    ): DefaultResult<ClassDto?> {
        return execute2(
            contentType = ContentType.Category,
            mapper = { items -> items.map { it.toObject<ClassDto>() }.firstOrNull() }
        ) {
            Firebase.firestore.collection("Classes")
                .where(Filter.equalTo("id",itemId))
                .get(sourceType.toFirebaseSource()).await()
        }
    }

    override suspend fun getOrders(
        kingdomType: KingdomType,
        classId: Int?,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): DefaultResult<List<OrderDto>> {
        return execute(
            contentType = ContentType.Category,
            orderDirection = loadType.orderDirection,
            mapper = { items -> items.map { it.toObject<OrderDto>()} }
        ) {
            var query = Firebase.firestore.collection("Orders")
                .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
            if(classId != null){
                query = query.where(Filter.equalTo("class_id",classId))
            }
            query
                .customBuild(loadKey = loadKey, loadType = loadType, limit = limit)
                .get(sourceType.toFirebaseSource())
                .await()
        }
    }

    override suspend fun getOrders(
        itemIds: List<Int>,
        orderByKey: String?,
        incrementCounter: Boolean
    ): DefaultResult<List<OrderDto>> {
        return execute(
            contentType = ContentType.Category,
            incrementCounter = incrementCounter,
            mapper = { items -> items.map { it.toObject<OrderDto>() } }
        )   {
            Firebase.firestore.collection("Orders")
                .whereIn("id", itemIds)
                .customBuild(limit = itemIds.size, orderByKey = orderByKey)
                .get()
                .await()
        }
    }

    override suspend fun getOrderById(
        itemId: Int,
        sourceType: RemoteSourceType
    ): DefaultResult<OrderDto?> {
        return execute2(
            contentType = ContentType.Category,
            mapper = { items -> items.map { it.toObject<OrderDto>() }.firstOrNull() }
        ) {
            Firebase.firestore.collection("Orders")
                .where(Filter.equalTo("id",itemId))
                .get(sourceType.toFirebaseSource()).await()
        }
    }

    override suspend fun getFamilies(
        kingdomType: KingdomType,
        orderId: Int?,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): DefaultResult<List<FamilyDto>> {
        return execute(
            contentType = ContentType.Category,
            orderDirection = loadType.orderDirection,
            mapper = { items -> items.map { it.toObject<FamilyDto>()} }
        ){
            var query = Firebase.firestore.collection("Families")
                .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
            if(orderId != null){
                query = query.where(Filter.equalTo("order_id",orderId))
            }
            query
                .customBuild(loadKey = loadKey, loadType = loadType, limit = limit)
                .get(sourceType.toFirebaseSource())
                .await()
        }
    }

    override suspend fun getFamilies(
        itemIds: List<Int>,
        orderByKey: String?,
        incrementCounter: Boolean
    ): DefaultResult<List<FamilyDto>> {
        return execute(
            contentType = ContentType.Category,
            incrementCounter = incrementCounter,
            mapper = { items -> items.map { it.toObject<FamilyDto>() } }
        )   {
            Firebase.firestore.collection("Families")
                .whereIn("id", itemIds)
                .customBuild(limit = itemIds.size, orderByKey = orderByKey)
                .get()
                .await()
        }
    }

    override suspend fun getFamilyById(
        itemId: Int,
        sourceType: RemoteSourceType
    ): DefaultResult<FamilyDto?> {
        return execute2(
            contentType = ContentType.Category,
            mapper = { items -> items.map { it.toObject<FamilyDto>() }.firstOrNull() }
        ) {
            Firebase.firestore.collection("Families")
                .where(Filter.equalTo("id",itemId))
                .get(sourceType.toFirebaseSource()).await()
        }
    }

    override suspend fun getHabitats(
        kingdomType: KingdomType,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): DefaultResult<List<HabitatCategoryDto>> {
        return execute(
            contentType = ContentType.Category,
            orderDirection = loadType.orderDirection,
            mapper = { items -> items.map { it.toObject<HabitatCategoryDto>()} }
        ){
            Firebase.firestore.collection("Habitats")
                .where(Filter.arrayContains("kingdom_ids", kingdomType.kingdomId))
                .customBuild(loadKey = loadKey, loadType = loadType, limit = limit, orderByKey = "id")
                .get(sourceType.toFirebaseSource())
                .await()
        }
    }

    override suspend fun getHabitatById(
        itemId: Int,
        sourceType: RemoteSourceType
    ): DefaultResult<HabitatCategoryDto?> {
        return execute2(
            contentType = ContentType.Category,
            mapper = { items -> items.map { it.toObject<HabitatCategoryDto>() }.firstOrNull() }
        ) {
            Firebase.firestore.collection("Habitats")
                .where(Filter.equalTo("id",itemId))
                .get(sourceType.toFirebaseSource()).await()
        }
    }

    override suspend fun getHabitatsByIds(
        itemIds: List<Int>,
        loadKey: Int?,
        limit: Int,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): DefaultResult<List<HabitatCategoryDto>> {
        return execute(
            contentType = ContentType.Category,
            orderDirection = loadType.orderDirection,
            mapper = { items -> items.map { it.toObject<HabitatCategoryDto>()} }
        ) {
            Firebase.firestore.collection("Habitats")
                .whereIn("id", itemIds)
                .customBuild(loadKey = loadKey, loadType = loadType, limit = limit, orderByKey = "id")
                .get(sourceType.toFirebaseSource())
                .await()
        }
    }

    private suspend inline fun <reified T> execute(
        contentType: ContentType,
        orderDirection: OrderDirection = OrderDirection.ASCENDING,
        incrementCounter: Boolean = true,
        crossinline mapper: suspend (List<QueryDocumentSnapshot>) -> T,
        crossinline execute: suspend () -> QuerySnapshot
    ): Result<T, ErrorText> {
        return safeCall {
            val result = execute()
            if(incrementCounter){
                val isFromCache = result.metadata.isFromCache
                val counter: Int = when{
//                isFromCache -> 0
                    else -> maxOf(result.size(), 1)
                }
                serverReadCounter.addCounter(contentType, counter)
            }
            if(orderDirection.isDescending){
                mapper(result.reversed())
            }else{
                mapper(result.mapNotNull { it })
            }
        }
    }

    private suspend inline fun <reified T> execute2(
        contentType: ContentType,
        crossinline mapper: suspend (List<QueryDocumentSnapshot>) -> T,
        crossinline execute: suspend () -> QuerySnapshot
    ): Result<T, ErrorText> {
        return execute(
            contentType = contentType,
            orderDirection = OrderDirection.ASCENDING,
            mapper = mapper,
            execute = execute
        )
    }

}


private fun Query.customBuild(
    limit: Int,
    loadKey: Int? = null,
    loadType: RemoteLoadType = RemoteLoadType.APPEND,
    orderByKey: String? = "order_key"
): Query{
    var baseQuery = this

    if(orderByKey != null){
        baseQuery = baseQuery
            .orderBy(orderByKey,if(loadType.orderDirection.isDescending)Query.Direction.DESCENDING else Query.Direction.ASCENDING)
    }

    val finalQuery = when {
        loadType.isRefresh -> baseQuery.startAt(loadKey)
        loadKey != null -> baseQuery.startAfter(loadKey)
        else -> baseQuery
    }
    return finalQuery
        .limit(limit.toLong())
}