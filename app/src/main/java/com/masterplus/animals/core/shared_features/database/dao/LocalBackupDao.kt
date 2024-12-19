package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.*
import com.masterplus.animals.core.shared_features.database.entity.HistoryEntity
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.database.entity.ListSpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.SavePointEntity

@Dao
interface LocalBackupDao {

    @Query("""select * from histories""")
    suspend fun getHistories(): List<HistoryEntity>

    @Query("""select * from lists""")
    suspend fun getLists(): List<ListEntity>

    @Query("""select * from listSpecies""")
    suspend fun getListSpecies(): List<ListSpeciesEntity>

    @Query("""select * from savePoints""")
    suspend fun getSavePoints(): List<SavePointEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistories(histories: List<HistoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<ListEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListSpecies(listSpecies: List<ListSpeciesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavePoints(savePoints: List<SavePointEntity>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ListEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListSpecies(listSpecies: ListSpeciesEntity): Long


    @Query("""delete from savePoints""")
    suspend fun deleteSavePointsWithQuery()

    @Query("""delete from lists""")
    suspend fun deleteListsWithQuery()

    @Query("""delete from listSpecies""")
    suspend fun deleteListSpeciesWithQuery()

    @Query("""delete from histories""")
    suspend fun deleteHistoriesWithQuery()


    @Query("""select ifnull(max(pos),0) from lists""")
    suspend fun getListMaxPos(): Int


    @Transaction
    suspend fun deleteUserData(){
        deleteListSpeciesWithQuery()
        deleteSavePointsWithQuery()
        deleteListsWithQuery()
        deleteHistoriesWithQuery()
    }

}