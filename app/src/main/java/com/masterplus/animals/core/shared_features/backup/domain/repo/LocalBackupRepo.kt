package com.masterplus.animals.core.shared_features.backup.domain.repo

interface LocalBackupRepo {

    suspend fun getJsonData(): String?

    suspend fun fromJsonData(data: String, removeAllData: Boolean, addOnLocalData: Boolean)

    suspend fun deleteAllLocalUserData()
}