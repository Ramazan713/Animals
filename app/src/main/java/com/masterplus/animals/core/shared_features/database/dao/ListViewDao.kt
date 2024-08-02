package com.masterplus.trdictionary.core.data.local.services

import androidx.room.Dao
import androidx.room.Query
import com.masterplus.animals.core.shared_features.database.view.ListViewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListViewDao {

    @Query("""
        select * from listViews where isArchive = :isArchive
        order by isRemovable, listPos desc 
    """)
    fun getListViews(isArchive: Boolean): Flow<List<ListViewEntity>>

    @Query("""select * from listViews where isRemovable = 1 order by listPos desc""")
    fun getRemovableAllListViews(): Flow<List<ListViewEntity>>

    @Query("""
        select * from listViews where isRemovable = 1 and isArchive = :isArchive
        order by listPos desc
    """)
    fun getRemovableListViews(isArchive: Boolean): Flow<List<ListViewEntity>>


    @Query("""select * from listViews where isRemovable = 0 order by id desc limit 1""")
    suspend fun getFavoriteList(): ListViewEntity?

    @Query(
        """
        select LV.* from listViews LV, listanimals LA 
        where LV.id = LA.listId and LA.animalId = :animalId
    """
    )
    fun getListViewsByAnimalId(animalId: Int): Flow<List<ListViewEntity>>


}