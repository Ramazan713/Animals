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

    @Query("SELECT * FROM remotekeys WHERE label = :query")
    suspend fun remoteKeyByQuery(query: String): RemoteKeyEntity?

    @Query("DELETE FROM remotekeys WHERE label = :query")
    suspend fun deleteByQuery(query: String)


    @Query("""
        update remotekeys set should_refresh = :shouldRefresh where label = :label
    """)
    suspend fun setShouldRefresh(label: String, shouldRefresh: Boolean)

    @Query("""
        update remotekeys set is_next_key_end = :nextKeyEnd
    """)
    suspend fun setAllNextKeyEnd(nextKeyEnd: Boolean)

    @Query("""
        update remotekeys set should_refresh = :shouldRefresh
    """)
    suspend fun setAllShouldRefresh(shouldRefresh: Boolean)


}