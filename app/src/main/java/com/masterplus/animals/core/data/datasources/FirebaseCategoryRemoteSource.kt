package com.masterplus.animals.core.data.datasources

import ClassDto
import FamilyDto
import HabitatCategoryDto
import OrderDto
import PhylumDto
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.mapper.toClassWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toFamilyWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toHabitatCategoryWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toOrderWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toPhylumWithImageEmbedded
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.ErrorText
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.safeCall
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.database.entity_helper.ClassWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.FamilyWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.HabitatWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.OrderWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.PhylumWithImageEmbedded
import kotlinx.coroutines.tasks.await

class FirebaseCategoryRemoteSource(
    private val serverReadCounter: ServerReadCounter
): CategoryRemoteSource {

    override suspend fun getSpeciesByKingdom(
        kingdomType: KingdomType,
        limit: Int,
        startAfter: Int?
    ): DefaultResult<List<SpeciesDto>> {
        return execute(ContentType.Content) {
            Firebase.firestore.collection("Species")
                .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
                .limit(limit.toLong())
                .orderBy("id")
                .startAfter(startAfter)
                .get()
                .await()
                .toObjects(SpeciesDto::class.java)
        }
    }

    override suspend fun getSpeciesCategories(
        categoryType: CategoryType,
        itemId: Int,
        limit: Int,
        startAfter: Int?
    ): DefaultResult<List<SpeciesDto>> {
        val filter = when(categoryType){
            CategoryType.Class -> Filter.equalTo("class_id", itemId)
            CategoryType.Order -> Filter.equalTo("order_id", itemId)
            CategoryType.Family -> Filter.equalTo("family_id", itemId)
            CategoryType.Habitat -> Filter.arrayContains("habitats", itemId)
            else -> null
        }
        return execute(ContentType.Content)  {
            Firebase.firestore.collection("Species")
                .where(filter!!)
                .limit(limit.toLong())
                .orderBy("id")
                .startAfter(startAfter)
                .get()
                .await()
                .toObjects(SpeciesDto::class.java)
        }
    }

    override suspend fun getSpecies(itemIds: List<Int>, limit: Int): DefaultResult<List<SpeciesDto>> {
        return execute(ContentType.Content)  {
            Firebase.firestore.collection("Species")
                .whereIn("id", itemIds)
                .limit(limit.toLong())
                .orderBy("id")
                .get()
                .await()
                .toObjects(SpeciesDto::class.java)
        }
    }

    override suspend fun getPhylums(
        kingdomType: KingdomType,
        limit: Int,
        startAfter: Int?,
        label: String
    ): DefaultResult<List<PhylumWithImageEmbedded>> {
        return execute(ContentType.Category)  {
            Firebase.firestore.collection("Phylums")
                .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
                .limit(limit.toLong())
                .orderBy("id")
                .startAfter(startAfter)
                .get()
                .await()
                .toObjects(PhylumDto::class.java)
                .map { it.toPhylumWithImageEmbedded(label) }
        }
    }

    override suspend fun getPhylumById(
        itemId: Int,
        label: String
    ): DefaultResult<PhylumWithImageEmbedded?> {
        return execute(ContentType.Category) {
            val x = Firebase.firestore.collection("Phylums")
                .document("$itemId")
                .get().await().toObject(PhylumDto::class.java)
                ?.toPhylumWithImageEmbedded(label)
            x
        }
    }

    override suspend fun getClasses(
        kingdomType: KingdomType,
        limit: Int,
        phylumId: Int?,
        startAfter: Int?,
        label: String
    ): DefaultResult<List<ClassWithImageEmbedded>> {
        return execute(ContentType.Category) {
            var query = Firebase.firestore.collection("Classes")
                .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
            if(phylumId != null){
                query = query.where(Filter.equalTo("phylum_id",phylumId))
            }
            query
                .limit(limit.toLong())
                .orderBy("id")
                .startAfter(startAfter)
                .get()
                .await()
                .toObjects(ClassDto::class.java)
                .map { it.toClassWithImageEmbedded(label) }
        }
    }

    override suspend fun getClassById(
        itemId: Int,
        label: String
    ): DefaultResult<ClassWithImageEmbedded?> {
        return execute(ContentType.Category) {
            Firebase.firestore.collection("Classes")
                .document("$itemId")
                .get().await().toObject(ClassDto::class.java)
                ?.toClassWithImageEmbedded(label)
        }
    }

    override suspend fun getOrders(
        kingdomType: KingdomType,
        limit: Int,
        classId: Int?,
        startAfter: Int?,
        label: String
    ): DefaultResult<List<OrderWithImageEmbedded>> {
        return execute(ContentType.Category) {
            var query = Firebase.firestore.collection("Orders")
                .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
            if(classId != null){
                query = query.where(Filter.equalTo("class_id",classId))
            }
            query
                .limit(limit.toLong())
                .orderBy("id")
                .startAfter(startAfter)
                .get()
                .await()
                .toObjects(OrderDto::class.java)
                .map { it.toOrderWithImageEmbedded(label) }
        }
    }

    override suspend fun getOrderById(
        itemId: Int,
        label: String
    ): DefaultResult<OrderWithImageEmbedded?> {
        return execute(ContentType.Category) {
            Firebase.firestore.collection("Orders")
                .document("$itemId")
                .get().await().toObject(OrderDto::class.java)
                ?.toOrderWithImageEmbedded(label)
        }
    }

    override suspend fun getFamilies(
        kingdomType: KingdomType,
        limit: Int,
        orderId: Int?,
        startAfter: Int?,
        label: String
    ): DefaultResult<List<FamilyWithImageEmbedded>> {
        return execute(ContentType.Category) {
            var query = Firebase.firestore.collection("Families")
                .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
            if(orderId != null){
                query = query.where(Filter.equalTo("order_id",orderId))
            }
            query
                .limit(limit.toLong())
                .orderBy("id")
                .startAfter(startAfter)
                .get()
                .await()
                .toObjects(FamilyDto::class.java)
                .map { it.toFamilyWithImageEmbedded(label) }
        }
    }

    override suspend fun getFamilyById(
        itemId: Int,
        label: String
    ): DefaultResult<FamilyWithImageEmbedded?> {
        return execute(ContentType.Category) {
            Firebase.firestore.collection("Families")
                .document("$itemId")
                .get().await().toObject(FamilyDto::class.java)
                ?.toFamilyWithImageEmbedded(label)
        }
    }

    override suspend fun getHabitats(
        kingdomType: KingdomType,
        limit: Int,
        startAfter: Int?,
        label: String
    ): DefaultResult<List<HabitatWithImageEmbedded>> {
        return execute(ContentType.Category) {
            Firebase.firestore.collection("Habitats")
                //TODO: add filter with kingdomId
//                .where(Filter.equalTo("kingdom_id", kingdomType.kingdomId))
                .limit(limit.toLong())
                .orderBy("id")
                .startAfter(startAfter)
                .get()
                .await()
                .toObjects(HabitatCategoryDto::class.java)
                .map { it.toHabitatCategoryWithImageEmbedded(label) }
        }
    }

    override suspend fun getHabitatById(
        itemId: Int,
        label: String
    ): DefaultResult<HabitatWithImageEmbedded?> {
        return execute(ContentType.Category) {
            Firebase.firestore.collection("Habitats")
                .document("$itemId")
                .get().await().toObject(HabitatCategoryDto::class.java)
                ?.toHabitatCategoryWithImageEmbedded(label)
        }
    }

    override suspend fun getHabitatsByIds(
        itemIds: List<Int>,
        label: String
    ): DefaultResult<List<HabitatWithImageEmbedded>> {
        return execute(ContentType.Category) {
            Firebase.firestore.collection("Habitats")
                .whereIn("id", itemIds)
                .get().await().toObjects(HabitatCategoryDto::class.java)
                .map { it.toHabitatCategoryWithImageEmbedded(label) }
        }
    }

    private suspend inline fun <reified T> execute(
        contentType: ContentType,
        crossinline execute: suspend () -> T
    ): Result<T, ErrorText> {
        return safeCall {
            val result = execute()
            val counter: Int =  if(result is List<*>) maxOf(result.size, 1) else 1
            serverReadCounter.addCounter(contentType, counter)
            result
        }
    }

}