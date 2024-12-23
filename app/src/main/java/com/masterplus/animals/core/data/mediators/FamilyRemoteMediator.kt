package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity_helper.FamilyWithImageEmbedded

class FamilyRemoteMediator(
    db: AppDatabase,
    private val kingdomType: KingdomType,
    private val orderId: Int?,
    private val categoryRemoteSource: CategoryRemoteSource,
    private val limit: Int
): BaseRemoteMediator<FamilyWithImageEmbedded>(db) {

    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getFamilyRemoteKey(
            kingdomType = kingdomType,
            orderId = orderId
        )

    override suspend fun fetchData(startAfter: Int?): DefaultResult<List<FamilyWithImageEmbedded>> {
        return categoryRemoteSource.getFamilies(
            kingdomType = kingdomType,
            limit = limit,
            orderId = orderId,
            startAfter = startAfter
        )
    }

    override suspend fun insertData(items: List<FamilyWithImageEmbedded>) {
        db.categoryDao.insertFamiliesWithImages(items)
    }

    override fun getNextKey(items: List<FamilyWithImageEmbedded>): Int? {
        return items.lastOrNull()?.family?.id
    }

    override suspend fun clearTable() {
        if(orderId != null){
            db.categoryDao.deleteFamilies(orderId, kingdomType.kingdomId)
        }else{
            db.categoryDao.deleteFamilies(kingdomType.kingdomId)
        }
    }
}