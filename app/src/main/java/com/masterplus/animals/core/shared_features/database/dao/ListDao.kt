package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.masterplus.animals.core.shared_features.database.entity.ListEntity

@Dao
interface ListDao {

    @Query("""select * from lists where id = :listId""")
    suspend fun getListById(listId: Int): ListEntity?

    @Query("""select ifnull(max(pos),0) from lists""")
    suspend fun getMaxPos(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(listEntity: ListEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateList(listEntity: ListEntity)

    @Query("""delete from lists where id = :id""")
    suspend fun deleteListById(id: Int)
}