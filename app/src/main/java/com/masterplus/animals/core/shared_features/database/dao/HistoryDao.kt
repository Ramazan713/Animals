package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.masterplus.animals.core.shared_features.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateHistory(history: HistoryEntity)

    @Query(
        """
        select * from histories where history_type_id = :typeId and lang_code = :langCode
        order by modified_date_time desc
    """
    )
    fun getFlowHistories(typeId: Int, langCode: String): Flow<List<HistoryEntity>>

    @Query("""
        select * from histories where history_type_id = :typeId and lang_code = :langCode and content = :content
    """)
    suspend fun getHistory(typeId: Int, langCode: String, content: String): HistoryEntity?

    @Query("""
        delete from histories where id = :id
    """)
    suspend fun deleteHistoryById(id: Int)
}