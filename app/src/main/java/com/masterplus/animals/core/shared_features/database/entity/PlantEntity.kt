package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Plants",
    foreignKeys = [
        ForeignKey(
            entity = SpeciesEntity::class,
            parentColumns = ["id"],
            childColumns = ["species_id"],
        )
    ]
)
data class PlantEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val species_id: Int,
    val size_c_en: String?,
    val size_c_tr: String?,
    val color_c_en: String?,
    val color_c_tr: String?,
    val flowering_time_c_en: String?,
    val flowering_time_c_tr: String?,
    val cultural_importance_c_en: String?,
    val cultural_importance_c_tr: String?,
    val conservation_status_c_en: String?,
    val conservation_status_c_tr: String?,
    val medicinal_benefits_c_en: String?,
    val medicinal_benefits_c_tr: String?,
    val potential_harm_c_en: String?,
    val potential_harm_c_tr: String?,
    val physical_characteristics_en: String?,
    val physical_characteristics_tr: String?,
    val natural_habitat_en: String?,
    val natural_habitat_tr: String?,
    val ecosystem_en: String?,
    val ecosystem_tr: String?,
    val growth_conditions_en: String?,
    val growth_conditions_tr: String?,
    val development_process_en: String?,
    val development_process_tr: String?,
    val flowering_time_and_characteristics_en: String?,
    val flowering_time_and_characteristics_tr: String?,
    val reproduction_methods_en: String?,
    val reproduction_methods_tr: String?,
    val role_in_ecosystem_en: String?,
    val role_in_ecosystem_tr: String?,
    val economic_value_en: String?,
    val economic_value_tr: String?,
    val environmental_adaptations_en: String?,
    val environmental_adaptations_tr: String?,
    val evolutionary_processes_en: String?,
    val evolutionary_processes_tr: String?,
    val cultural_context_en: String?,
    val cultural_context_tr: String?,
    val historical_usage_en: String?,
    val historical_usage_tr: String?,
    val threats_and_dangers_en: String?,
    val threats_and_dangers_tr: String?,
    val conservation_efforts_en: String?,
    val conservation_efforts_tr: String?,
    val medicinal_uses_and_benefits_en: String?,
    val medicinal_uses_and_benefits_tr: String?,
    val potential_harm_and_side_effects_en: String?,
    val potential_harm_and_side_effects_tr: String?,
    val noteworthy_characteristics_en: String?,
    val noteworthy_characteristics_tr: String?,
    val surprising_facts_en: String?,
    val surprising_facts_tr: String?,
    val interesting_and_fun_facts_en: String?,
    val interesting_and_fun_facts_tr: String?
)
