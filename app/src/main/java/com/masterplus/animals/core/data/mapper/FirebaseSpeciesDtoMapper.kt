package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.data.dtos.SpeciesAnimalDto
import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.dtos.SpeciesPlantDto
import com.masterplus.animals.core.shared_features.database.entity.AnimalEntity
import com.masterplus.animals.core.shared_features.database.entity.PlantEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesHabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDataEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.PlantDataEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesImageWithMetadataEmbedded


fun SpeciesDto.toAnimalDataEmbedded(label: String): AnimalDataEmbedded{
    return AnimalDataEmbedded(
        species = toSpeciesEntity(label),
        images = toSpeciesImageWithMetadataEmbedded(label),
        animal = animalia?.toAnimalEntity(id,label)!!
    )
}

fun SpeciesDto.toPlantDataEmbedded(label: String): PlantDataEmbedded {
    return PlantDataEmbedded(
        species = toSpeciesEntity(label),
        images = toSpeciesImageWithMetadataEmbedded(label),
        plant = plantae?.toPlantEntity(id, label)!!
    )
}

fun SpeciesDto.toSpeciesHabitatCategoryEntity(): List<SpeciesHabitatCategoryEntity>{
    return habitats.map { habitatId ->
        SpeciesHabitatCategoryEntity(
            category_id = habitatId,
            species_id = id
        )
    }
}

fun SpeciesDto.toSpeciesImageWithMetadataEmbedded(label: String): List<SpeciesImageWithMetadataEmbedded>{
    return images.mapIndexed { index, image ->
        SpeciesImageWithMetadataEmbedded(
            speciesImage = SpeciesImageEntity(
                species_id = id,
                image_id = image.id,
                image_order = image.image_order ?: index,
                label = label
            ),
            imageWithMetadata = image.toImageWithMetadata()
        )
    }
}


fun SpeciesDto.toSpeciesEntity(label: String): SpeciesEntity{
    return SpeciesEntity(
        id = id,
        name_en = name_en,
        name_tr = name_tr,
        family_id = family_id,
        kingdom_id = kingdom_id,
        introduction_en = introduction_en,
        introduction_tr = introduction_tr,
        recognition_and_interaction = recognition_and_interaction,
        scientific_name = scientific_name,
        updated_at = "",
        created_at = "",
        order_id = order_id,
        class_id = class_id,
        phylum_id = phylum_id,
        label = label,
        order_key = pos
    )
}

fun SpeciesAnimalDto.toAnimalEntity(speciesId: Int,label: String): AnimalEntity {
    return AnimalEntity(
        id = null,
        species_id = speciesId,
        size_c_en = en.size_c_en,
        size_c_tr = tr.size_c_tr,
        weight_c_en = en.weight_c_en,
        weight_c_tr = tr.weight_c_tr,
        color_c_en = en.color_c_en,
        color_c_tr = tr.color_c_tr,
        feeding_c_en = en.feeding_c_en,
        feeding_c_tr = tr.feeding_c_tr,
        threats_c_en = en.threats_c_en,
        threats_c_tr = tr.threats_c_tr,
        conservation_status_c_en = en.conservation_status_c_en,
        conservation_status_c_tr = tr.conservation_status_c_tr,
        cultural_significance_c_en = en.cultural_significance_c_en,
        cultural_significance_c_tr = tr.cultural_significance_c_tr,
        physical_characteristics_en = en.physical_characteristics_en,
        physical_characteristics_tr = tr.physical_characteristics_tr,
        natural_habitat_en = en.natural_habitat_en,
        natural_habitat_tr = tr.natural_habitat_tr,
        ecosystem_en = en.ecosystem_en,
        ecosystem_tr = tr.ecosystem_tr,
        feeding_habits_en = en.feeding_habits_en,
        feeding_habits_tr = tr.feeding_habits_tr,
        social_structure_and_behaviors_en = en.social_structure_and_behaviors_en,
        social_structure_and_behaviors_tr = tr.social_structure_and_behaviors_tr,
        reproductive_behaviors_en = en.reproductive_behaviors_en,
        reproductive_behaviors_tr = tr.reproductive_behaviors_tr,
        development_of_the_young_en = en.development_of_the_young_en,
        development_of_the_young_tr = tr.development_of_the_young_tr,
        sounds_produced_en = en.sounds_produced_en,
        sounds_produced_tr = tr.sounds_produced_tr,
        communication_methods_en = en.communication_methods_en,
        communication_methods_tr = tr.communication_methods_tr,
        threats_and_dangers_en = en.threats_and_dangers_en,
        threats_and_dangers_tr = tr.threats_and_dangers_tr,
        conservation_efforts_en = en.conservation_efforts_en,
        conservation_efforts_tr = tr.conservation_efforts_tr,
        conservation_challenges_en = en.conservation_challenges_en,
        conservation_challenges_tr = tr.conservation_challenges_tr,
        cultural_and_historical_significance_en = en.cultural_and_historical_significance_en,
        cultural_and_historical_significance_tr = tr.cultural_and_historical_significance_tr,
        economic_and_scientific_importance_en = en.economic_and_scientific_importance_en,
        economic_and_scientific_importance_tr = tr.economic_and_scientific_importance_tr,
        modern_day_perception_en = en.modern_day_perception_en,
        modern_day_perception_tr = tr.modern_day_perception_tr,
        environmental_adaptations_en = en.environmental_adaptations_en,
        environmental_adaptations_tr = tr.environmental_adaptations_tr,
        evolutionary_processes_en = en.evolutionary_processes_en,
        evolutionary_processes_tr = tr.evolutionary_processes_tr,
        observed_interesting_behaviors_en = en.observed_interesting_behaviors_en,
        observed_interesting_behaviors_tr = tr.observed_interesting_behaviors_tr,
        ethological_insights_en = en.ethological_insights_en,
        ethological_insights_tr = tr.ethological_insights_tr,
        interesting_and_fun_facts_en = en.interesting_and_fun_facts_en,
        interesting_and_fun_facts_tr = tr.interesting_and_fun_facts_tr,
        comparative_analysis_en = en.comparative_analysis_en,
        comparative_analysis_tr = tr.comparative_analysis_tr,
        role_in_ecosystem_en = en.role_in_ecosystem_en,
        role_in_ecosystem_tr = tr.role_in_ecosystem_tr,
        label = label
    )
}


fun SpeciesPlantDto.toPlantEntity(speciesId: Int,label: String): PlantEntity {
    return PlantEntity(
        id = null,
        species_id = speciesId,
        size_c_en = en.size_c_en,
        size_c_tr = tr.size_c_tr,
        color_c_en = en.color_c_en,
        color_c_tr = tr.color_c_tr,
        flowering_time_c_en = en.flowering_time_c_en,
        flowering_time_c_tr = tr.flowering_time_c_tr,
        cultural_importance_c_en = en.cultural_importance_c_en,
        cultural_importance_c_tr = tr.cultural_importance_c_tr,
        conservation_status_c_en = en.conservation_status_c_en,
        conservation_status_c_tr = tr.conservation_status_c_tr,
        medicinal_benefits_c_en = en.medicinal_benefits_c_en,
        medicinal_benefits_c_tr = tr.medicinal_benefits_c_tr,
        potential_harm_c_en = en.potential_harm_c_en,
        potential_harm_c_tr = tr.potential_harm_c_tr,
        physical_characteristics_en = en.physical_characteristics_en,
        physical_characteristics_tr = tr.physical_characteristics_tr,
        natural_habitat_en = en.natural_habitat_en,
        natural_habitat_tr = tr.natural_habitat_tr,
        ecosystem_en = en.ecosystem_en,
        ecosystem_tr = tr.ecosystem_tr,
        growth_conditions_en = en.growth_conditions_en,
        growth_conditions_tr = tr.growth_conditions_tr,
        development_process_en = en.development_process_en,
        development_process_tr = tr.development_process_tr,
        flowering_time_and_characteristics_en = en.flowering_time_and_characteristics_en,
        flowering_time_and_characteristics_tr = tr.flowering_time_and_characteristics_tr,
        reproduction_methods_en = en.reproduction_methods_en,
        reproduction_methods_tr = tr.reproduction_methods_tr,
        role_in_ecosystem_en = en.role_in_ecosystem_en,
        role_in_ecosystem_tr = tr.role_in_ecosystem_tr,
        economic_value_en = en.economic_value_en,
        economic_value_tr = tr.economic_value_tr,
        environmental_adaptations_en = en.environmental_adaptations_en,
        environmental_adaptations_tr = tr.environmental_adaptations_tr,
        evolutionary_processes_en = en.evolutionary_processes_en,
        evolutionary_processes_tr = tr.evolutionary_processes_tr,
        cultural_context_en = en.cultural_context_en,
        cultural_context_tr = tr.cultural_context_tr,
        historical_usage_en = en.historical_usage_en,
        historical_usage_tr = tr.historical_usage_tr,
        threats_and_dangers_en = en.threats_and_dangers_en,
        threats_and_dangers_tr = tr.threats_and_dangers_tr,
        conservation_efforts_en = en.conservation_efforts_en,
        conservation_efforts_tr = tr.conservation_efforts_tr,
        medicinal_uses_and_benefits_en = en.medicinal_uses_and_benefits_en,
        medicinal_uses_and_benefits_tr = tr.medicinal_uses_and_benefits_tr,
        potential_harm_and_side_effects_en = en.potential_harm_and_side_effects_en,
        potential_harm_and_side_effects_tr = tr.potential_harm_and_side_effects_tr,
        noteworthy_characteristics_en = en.noteworthy_characteristics_en,
        noteworthy_characteristics_tr = tr.noteworthy_characteristics_tr,
        surprising_facts_en = en.surprising_facts_en,
        surprising_facts_tr = tr.surprising_facts_tr,
        interesting_and_fun_facts_en = en.interesting_and_fun_facts_en,
        interesting_and_fun_facts_tr = tr.interesting_and_fun_facts_tr,
        label = label
    )
}
