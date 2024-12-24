package com.masterplus.animals.core.shared_features.database.view

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "HabitatKingdomView",
    value = """
        SELECT DISTINCT hc.*, s.kingdom_id FROM HabitatCategories hc
        JOIN SpeciesHabitatCategories shc ON hc.id = shc.category_id
        JOIN Species s ON shc.species_id = s.id
    """)
data class HabitatKingdomView(
    val id: Int,
    val label: String,
    val habitat_category_en: String,
    val habitat_category_tr: String,
    val image_id: Int?,
    val kingdom_id: Int
)