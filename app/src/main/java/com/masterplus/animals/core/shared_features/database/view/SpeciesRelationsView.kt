package com.masterplus.animals.core.shared_features.database.view

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "SpeciesRelationsView",
    value = """
        select K.id kingdom_id, P.id phylum_id, C.id class_id, O.id order_id, F.id family_id, 
        G.id genuse_id, S.id species_id
        FROM Kingdoms K
        inner join Phylums P on K.id = P.kingdom_id
        inner join Classes C on P.id = C.phylum_id
        inner join Orders O on C.id = O.class_id
        inner join Families F on O.id = F.order_id
        inner join Genuses G on F.id = G.family_id
        inner join Species S  on G.id = S.genus_id
""")
data class SpeciesRelationsView(
    val kingdom_id: Int,
    val phylum_id: Int,
    val class_id: Int,
    val order_id: Int,
    val family_id: Int,
    val genuse_id: Int,
    val species_id: Int
)
