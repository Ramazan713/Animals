package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Animals",
    foreignKeys = [
        ForeignKey(
            entity = SpeciesEntity::class,
            parentColumns = ["id", "label"],
            childColumns = ["species_id", "label"],
        )
    ]
)
data class AnimalEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val label: String,
    val species_id: Int,
    val size_c_en: String?,
    val size_c_tr: String?,
    val weight_c_en: String?,
    val weight_c_tr: String?,
    val color_c_en: String?,
    val color_c_tr: String?,
    val feeding_c_en: String?,
    val feeding_c_tr: String?,
    val threats_c_en: String?,
    val threats_c_tr: String?,
    val conservation_status_c_en: String?,
    val conservation_status_c_tr: String?,
    val cultural_significance_c_en: String?,
    val cultural_significance_c_tr: String?,
    val physical_characteristics_en: String?,
    val physical_characteristics_tr: String?,
    val natural_habitat_en: String?,
    val natural_habitat_tr: String?,
    val ecosystem_en: String?,
    val ecosystem_tr: String?,
    val feeding_habits_en: String?,
    val feeding_habits_tr: String?,
    val social_structure_and_behaviors_en: String?,
    val social_structure_and_behaviors_tr: String?,
    val reproductive_behaviors_en: String?,
    val reproductive_behaviors_tr: String?,
    val development_of_the_young_en: String?,
    val development_of_the_young_tr: String?,
    val sounds_produced_en: String?,
    val sounds_produced_tr: String?,
    val communication_methods_en: String?,
    val communication_methods_tr: String?,
    val threats_and_dangers_en: String?,
    val threats_and_dangers_tr: String?,
    val conservation_efforts_en: String?,
    val conservation_efforts_tr: String?,
    val conservation_challenges_en: String?,
    val conservation_challenges_tr: String?,
    val cultural_and_historical_significance_en: String?,
    val cultural_and_historical_significance_tr: String?,
    val economic_and_scientific_importance_en: String?,
    val economic_and_scientific_importance_tr: String?,
    val modern_day_perception_en: String?,
    val modern_day_perception_tr: String?,
    val environmental_adaptations_en: String?,
    val environmental_adaptations_tr: String?,
    val evolutionary_processes_en: String?,
    val evolutionary_processes_tr: String?,
    val observed_interesting_behaviors_en: String?,
    val observed_interesting_behaviors_tr: String?,
    val ethological_insights_en: String?,
    val ethological_insights_tr: String?,
    val interesting_and_fun_facts_en: String?,
    val interesting_and_fun_facts_tr: String?,
    val comparative_analysis_en: String?,
    val comparative_analysis_tr: String?,
    val role_in_ecosystem_en: String?,
    val role_in_ecosystem_tr: String?
)
