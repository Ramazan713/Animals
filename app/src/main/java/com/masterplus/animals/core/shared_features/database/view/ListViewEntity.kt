package com.masterplus.animals.core.shared_features.database.view

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "listViews",
    value = """
     select L.id, L.name, L.isRemovable, L.isArchive, L.pos listPos,
     count(LS.speciesId) itemCounts, ifnull(max(LS.pos),0) contentMaxPos 
     from Lists L left join ListSpecies LS on  L.id = LS.listId
     group by L.id
""")
data class ListViewEntity(
    val id: Int?,
    val name: String,
    val isRemovable: Boolean,
    val isArchive: Boolean,
    val listPos: Int,
    val contentMaxPos: Int,
    val itemCounts: Int
)
