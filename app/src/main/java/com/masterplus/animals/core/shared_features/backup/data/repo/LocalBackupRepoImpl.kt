package com.masterplus.animals.core.shared_features.backup.data.repo

import com.masterplus.animals.core.shared_features.backup.domain.repo.LocalBackupRepo
import javax.inject.Inject

class LocalBackupRepoImpl @Inject constructor(
//    private val backupDao: LocalBackupDao,
//    private val backupParserRepo: BackupParserRepo,
//    private val settingsPreferences: SettingsPreferencesApp,
//    private val transactionProvider: TransactionProvider
): LocalBackupRepo {

    override suspend fun getJsonData(): String? {
        return null
    }


    override suspend fun fromJsonData(data: String, removeAllData: Boolean, addOnLocalData: Boolean){

    }

    override suspend fun deleteAllLocalUserData() {

    }
}