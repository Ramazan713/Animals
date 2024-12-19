package com.masterplus.animals.core.shared_features.backup.data.repo

import com.masterplus.animals.core.shared_features.backup.data.dtos.AllBackupData
import com.masterplus.animals.core.shared_features.backup.data.mapper.toBackupData
import com.masterplus.animals.core.shared_features.backup.data.mapper.toData
import com.masterplus.animals.core.shared_features.backup.data.mapper.toHistoryBackup
import com.masterplus.animals.core.shared_features.backup.data.mapper.toHistoryEntity
import com.masterplus.animals.core.shared_features.backup.data.mapper.toListBackup
import com.masterplus.animals.core.shared_features.backup.data.mapper.toListEntity
import com.masterplus.animals.core.shared_features.backup.data.mapper.toListSpeciesBackup
import com.masterplus.animals.core.shared_features.backup.data.mapper.toListSpeciesEntity
import com.masterplus.animals.core.shared_features.backup.data.mapper.toSavePointBackup
import com.masterplus.animals.core.shared_features.backup.data.mapper.toSavePointEntity
import com.masterplus.animals.core.shared_features.backup.domain.repo.LocalBackupRepo
import com.masterplus.animals.core.shared_features.database.TransactionProvider
import com.masterplus.animals.core.shared_features.database.dao.LocalBackupDao
import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import javax.inject.Inject

class LocalBackupRepoImpl @Inject constructor(
    private val backupDao: LocalBackupDao,
    private val backupParserRepo: BackupParserRepo,
    private val settingsPreferences: SettingsPreferences,
    private val transactionProvider: TransactionProvider
): LocalBackupRepo {

    override suspend fun getJsonData(): String? {
        val histories = backupDao.getHistories().map { it.toHistoryBackup() }
        val lists = backupDao.getLists().map { it.toListBackup() }
        val listSpecies = backupDao.getListSpecies().map { it.toListSpeciesBackup() }
        val savePoints = backupDao.getSavePoints().map { it.toSavePointBackup() }

        val settingsData = settingsPreferences.getData().toBackupData()

        val backupData = AllBackupData(
            histories = histories,
            lists = lists,
            listSpecies = listSpecies,
            savePoints = savePoints,
            settingsPreferences = settingsData
        )
        return backupParserRepo.toJson(backupData)
    }


    override suspend fun fromJsonData(data: String, removeAllData: Boolean, addOnLocalData: Boolean){
        val backupData = backupParserRepo.fromJson(data) ?: return

        transactionProvider.runAsTransaction {
            if(removeAllData){
                backupDao.deleteUserData()
            }
            backupDao.insertHistories(backupData.histories.map { it.toHistoryEntity() })

            if(addOnLocalData){
                val clearedListWordsEntity = backupData.listSpecies.map { it.toListSpeciesEntity() }
                var pos = backupDao.getListMaxPos() + 1
                backupData.lists.forEach { list->
                    val newListId = backupDao.insertList(list.copy(id = null, isRemovable = true, pos = pos).toListEntity()).toInt()
                    pos += 1
                    val updatedListContents = clearedListWordsEntity.filter { it.listId == list.id }
                        .map { it.copy(listId = newListId) }
                    backupDao.insertListSpecies(updatedListContents)
                }
            }else{
                backupDao.insertLists(backupData.lists.map { it.toListEntity() })
                backupDao.insertListSpecies(backupData.listSpecies.map { it.toListSpeciesEntity() })
            }
            backupDao.insertSavePoints(backupData.savePoints.map { it.toSavePointEntity() })

            backupData.settingsPreferences?.let { settings->
                settingsPreferences.updateData {
                    settings.toData()
                }
            }
        }
    }

    override suspend fun deleteAllLocalUserData() {
        backupDao.deleteUserData()
    }
}