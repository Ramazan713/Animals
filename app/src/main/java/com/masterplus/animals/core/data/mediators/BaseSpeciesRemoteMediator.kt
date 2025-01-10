package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.models.ItemOrder

abstract class BaseSpeciesRemoteMediator<T: ItemOrder>(
    config: RemoteMediatorConfig,
    targetItemId: Int?
): BaseRemoteMediator2<T, SpeciesDto>(config, targetItemId) {

    override val contentType: ContentType
        get() = ContentType.Content

    override suspend fun isItemExists(itemId: Int, label: String): Boolean {
        return db.speciesDao.getSpeciesByIdAndLabel(itemId, label) != null
    }

    override suspend fun insertData(items: List<SpeciesDto>) {
        val label = saveRemoteKey
        insertSpeciesHelper.insertSpecies(speciesList = items, label = label)
    }
}