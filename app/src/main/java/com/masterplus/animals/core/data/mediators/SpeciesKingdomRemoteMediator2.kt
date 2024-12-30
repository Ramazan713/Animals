package com.masterplus.animals.core.data.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.extensions.toRemoteLoadType
import com.masterplus.animals.core.data.mapper.toAnimalEntity
import com.masterplus.animals.core.data.mapper.toPlantEntity
import com.masterplus.animals.core.data.mapper.toSpeciesEntity
import com.masterplus.animals.core.data.mapper.toSpeciesHabitatCategoryEntity
import com.masterplus.animals.core.data.mapper.toSpeciesImageWithMetadataEmbedded
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.constants.K
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.ReadLimitExceededException
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity.RemoteKeyEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded
import kotlinx.coroutines.flow.firstOrNull
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class SpeciesKingdomRemoteMediator2(
    private val db: AppDatabase,
    private val categoryRemoteSource: CategoryRemoteSource,
    private val serverReadCounter: ServerReadCounter,
    private val kingdom: KingdomType,
    private val targetItemId: Int? = null
): RemoteMediator<Int, SpeciesDetailEmbedded>() {

    val saveRemoteKey: String
        get() = RemoteKeyUtil.getSpeciesKingdomRemoteKey(
            kingdom = kingdom
        )

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SpeciesDetailEmbedded>
    ): MediatorResult {
        return try {
            val remoteKey = db.withTransaction {
                db.remoteKeyDao.remoteKeyByQuery(saveRemoteKey)
            }

            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    if(targetItemId != null){
                        val item = db.speciesDao.getSpeciesByIdAndLabel(targetItemId, saveRemoteKey)
                        if(item != null) return MediatorResult.Success(endOfPaginationReached = false)
                        else targetItemId
                    }
                    else{
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }
                }
                LoadType.PREPEND ->{
                    getRemoteKeyForFirstItem(state, remoteKey) ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
                LoadType.APPEND -> {
                    val key = getRemoteKeyForLastItem(state, remoteKey)
                    if(remoteKey != null && key == null)return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                    key
                }
            }
            val counter = serverReadCounter.contentCountersFlow.firstOrNull() ?: 0
            if(counter > K.READ_EXCEED_LIMIT){
                return MediatorResult.Error(ReadLimitExceededException)
            }
            val dataResponse = categoryRemoteSource.getSpeciesByKingdom(
                kingdomType = kingdom,
                limit = state.config.pageSize,
                loadType = loadType.toRemoteLoadType(),
                loadKey = loadKey
            ).getSuccessData!!
            println("AppXXX state:contentCountersFlow:${counter} :")
            println("AppXXXX: mediator2: loadType: $loadType::$loadKey::refresh: ${targetItemId} ::remoteKey: ${remoteKey}  ::::response: ${getIds(dataResponse)}")
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.speciesDao.deleteSpeciesByLabel(saveRemoteKey)
                }
                val nextKey = when (loadType) {
                    LoadType.REFRESH, LoadType.APPEND -> dataResponse.lastOrNull()?.id?.toString()
                    else -> remoteKey?.nextKey
                }
                val prevKey = when (loadType) {
                    LoadType.REFRESH, LoadType.PREPEND -> dataResponse.firstOrNull()?.id?.toString()
                    else -> remoteKey?.prevKey
                }
                val updatedRemoteKey = remoteKey?.copy(
                    nextKey = nextKey,
                    prevKey = prevKey,
                ) ?: RemoteKeyEntity(
                    label = saveRemoteKey,
                    nextKey = nextKey,
                    prevKey = prevKey
                )

                db.remoteKeyDao.insertOrReplace(updatedRemoteKey)
                insertData(dataResponse)
            }
            MediatorResult.Success(
                endOfPaginationReached = dataResponse.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            println("AppXXXX errorx: $e")
            MediatorResult.Error(e)
        }
    }


    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, SpeciesDetailEmbedded>,  remoteKey: RemoteKeyEntity?): Int? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()?.species?.id ?: remoteKey?.nextKey?.toIntOrNull()
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, SpeciesDetailEmbedded>, remoteKey: RemoteKeyEntity?): Int? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()?.species?.id ?: remoteKey?.nextKey?.toIntOrNull()
    }

    fun getNextKey(items: List<SpeciesDto>): Int? {
        return items.lastOrNull()?.id
    }

    suspend fun getIds(items: List<SpeciesDto>): List<Int> {
        return items.map {
            it.id
        }
    }

    suspend fun insertData(items: List<SpeciesDto>) {
        val label = saveRemoteKey

        val species = items.map { it.toSpeciesEntity(label) }
        val speciesHabitats = items.flatMap { it.toSpeciesHabitatCategoryEntity() }
        val animals = items.mapNotNull { it.animalia?.toAnimalEntity(it.id, label) }
        val plants = items.mapNotNull { it.plantae?.toPlantEntity(it.id, label) }
        val speciesImageWithMetadata = items.flatMap { it.toSpeciesImageWithMetadataEmbedded(label) }
        val images = speciesImageWithMetadata.map { it.imageWithMetadata.image }
        val imageMetadatas = speciesImageWithMetadata.mapNotNull { it.imageWithMetadata.metadata }
        val speciesImages = speciesImageWithMetadata.map { it.speciesImage }

        db.categoryDao.insertImages(images)
        db.categoryDao.insertMetadatas(imageMetadatas)
        db.speciesDao.insertSpecies(species)
        db.speciesDao.insertSpeciesHabitats(speciesHabitats)
        db.animalDao.insertAnimals(animals)
        db.plantDao.insertPlants(plants)
        db.speciesDao.insertSpeciesImages(speciesImages)
    }
}