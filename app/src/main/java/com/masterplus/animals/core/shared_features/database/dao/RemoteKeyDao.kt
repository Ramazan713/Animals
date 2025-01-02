package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.masterplus.animals.core.shared_features.database.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeyEntity)

    @Query("SELECT * FROM remote_keys WHERE label = :query")
    suspend fun remoteKeyByQuery(query: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys WHERE label = :query")
    suspend fun deleteByQuery(query: String)


    @Query("""
        update remote_keys set shouldRefresh = :shouldRefresh where label = :label
    """)
    suspend fun setShouldRefresh(label: String, shouldRefresh: Boolean)

    @Query("""
        update remote_keys set isNextKeyEnd = :nextKeyEnd
    """)
    suspend fun setAllNextKeyEnd(nextKeyEnd: Boolean)

    @Query("""
        update remote_keys set shouldRefresh = :shouldRefresh
    """)
    suspend fun setAllShouldRefresh(shouldRefresh: Boolean)


}