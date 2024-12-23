package com.masterplus.animals.core.data.dtos

import ImageDto


data class SpeciesDto(
    val id: Int = 0,
    val scientific_name: String = "",
    val name_en: String = "",
    val name_tr: String = "",
    val family_id: Int = 0,
    val order_id: Int = 0,
    val class_id: Int = 0,
    val phylum_id: Int = 0,
    val kingdom_id: Int = 0,
    val introduction_en: String = "",
    val introduction_tr: String = "",
    val recognition_and_interaction: Int? = null,
    val images: List<ImageDto> = emptyList(),
    val habitats: List<Int> = emptyList(),
    val animalia: SpeciesAnimalDto? = null,
    val plantae: SpeciesPlantDto? = null
)

data class SpeciesAnimalDto(
    val en: SpeciesAnimalEnDto = SpeciesAnimalEnDto(),
    val tr: SpeciesAnimalTrDto = SpeciesAnimalTrDto()
)

data class SpeciesPlantDto(
    val en: SpeciesPlantEnDto = SpeciesPlantEnDto(),
    val tr: SpeciesPlantTrDto = SpeciesPlantTrDto()
)

data class SpeciesAnimalEnDto(
    val size_c_en: String? = null,
    val weight_c_en: String? = null,
    val color_c_en: String? = null,
    val feeding_c_en: String? = null,
    val threats_c_en: String? = null,
    val conservation_status_c_en: String? = null,
    val cultural_significance_c_en: String? = null,
    val physical_characteristics_en: String? = null,
    val natural_habitat_en: String? = null,
    val ecosystem_en: String? = null,
    val feeding_habits_en: String? = null,
    val social_structure_and_behaviors_en: String? = null,
    val reproductive_behaviors_en: String? = null,
    val development_of_the_young_en: String? = null,
    val sounds_produced_en: String? = null,
    val communication_methods_en: String? = null,
    val threats_and_dangers_en: String? = null,
    val conservation_efforts_en: String? = null,
    val conservation_challenges_en: String? = null,
    val cultural_and_historical_significance_en: String? = null,
    val economic_and_scientific_importance_en: String? = null,
    val modern_day_perception_en: String? = null,
    val environmental_adaptations_en: String? = null,
    val evolutionary_processes_en: String? = null,
    val observed_interesting_behaviors_en: String? = null,
    val ethological_insights_en: String? = null,
    val interesting_and_fun_facts_en: String? = null,
    val comparative_analysis_en: String? = null,
    val role_in_ecosystem_en: String? = null
)

data class SpeciesAnimalTrDto(
    val size_c_tr: String? = null,
    val weight_c_tr: String? = null,
    val color_c_tr: String? = null,
    val feeding_c_tr: String? = null,
    val threats_c_tr: String? = null,
    val conservation_status_c_tr: String? = null,
    val cultural_significance_c_tr: String? = null,
    val physical_characteristics_tr: String? = null,
    val natural_habitat_tr: String? = null,
    val ecosystem_tr: String? = null,
    val feeding_habits_tr: String? = null,
    val social_structure_and_behaviors_tr: String? = null,
    val reproductive_behaviors_tr: String? = null,
    val development_of_the_young_tr: String? = null,
    val sounds_produced_tr: String? = null,
    val communication_methods_tr: String? = null,
    val threats_and_dangers_tr: String? = null,
    val conservation_efforts_tr: String? = null,
    val conservation_challenges_tr: String? = null,
    val cultural_and_historical_significance_tr: String? = null,
    val economic_and_scientific_importance_tr: String? = null,
    val modern_day_perception_tr: String? = null,
    val environmental_adaptations_tr: String? = null,
    val evolutionary_processes_tr: String? = null,
    val observed_interesting_behaviors_tr: String? = null,
    val ethological_insights_tr: String? = null,
    val interesting_and_fun_facts_tr: String? = null,
    val comparative_analysis_tr: String? = null,
    val role_in_ecosystem_tr: String? = null
)

data class SpeciesPlantEnDto(
    val size_c_en: String? = null,
    val color_c_en: String? = null,
    val flowering_time_c_en: String? = null,
    val cultural_importance_c_en: String? = null,
    val conservation_status_c_en: String? = null,
    val medicinal_benefits_c_en: String? = null,
    val potential_harm_c_en: String? = null,
    val physical_characteristics_en: String? = null,
    val natural_habitat_en: String? = null,
    val ecosystem_en: String? = null,
    val growth_conditions_en: String? = null,
    val development_process_en: String? = null,
    val flowering_time_and_characteristics_en: String? = null,
    val reproduction_methods_en: String? = null,
    val role_in_ecosystem_en: String? = null,
    val economic_value_en: String? = null,
    val environmental_adaptations_en: String? = null,
    val evolutionary_processes_en: String? = null,
    val cultural_context_en: String? = null,
    val historical_usage_en: String? = null,
    val threats_and_dangers_en: String? = null,
    val conservation_efforts_en: String? = null,
    val medicinal_uses_and_benefits_en: String? = null,
    val potential_harm_and_side_effects_en: String? = null,
    val noteworthy_characteristics_en: String? = null,
    val surprising_facts_en: String? = null,
    val interesting_and_fun_facts_en: String? = null
)

data class SpeciesPlantTrDto(
    val size_c_tr: String? = null,
    val color_c_tr: String? = null,
    val flowering_time_c_tr: String? = null,
    val cultural_importance_c_tr: String? = null,
    val conservation_status_c_tr: String? = null,
    val medicinal_benefits_c_tr: String? = null,
    val potential_harm_c_tr: String? = null,
    val physical_characteristics_tr: String? = null,
    val natural_habitat_tr: String? = null,
    val ecosystem_tr: String? = null,
    val growth_conditions_tr: String? = null,
    val development_process_tr: String? = null,
    val flowering_time_and_characteristics_tr: String? = null,
    val reproduction_methods_tr: String? = null,
    val role_in_ecosystem_tr: String? = null,
    val economic_value_tr: String? = null,
    val environmental_adaptations_tr: String? = null,
    val evolutionary_processes_tr: String? = null,
    val cultural_context_tr: String? = null,
    val historical_usage_tr: String? = null,
    val threats_and_dangers_tr: String? = null,
    val conservation_efforts_tr: String? = null,
    val medicinal_uses_and_benefits_tr: String? = null,
    val potential_harm_and_side_effects_tr: String? = null,
    val noteworthy_characteristics_tr: String? = null,
    val surprising_facts_tr: String? = null,
    val interesting_and_fun_facts_tr: String? = null
)

