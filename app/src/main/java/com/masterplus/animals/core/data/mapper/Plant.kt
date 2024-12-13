package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.Plant
import com.masterplus.animals.core.domain.models.PlantDetail
import com.masterplus.animals.core.shared_features.database.entity.PlantEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.PlantDataEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.PlantDetailEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesImageWithMetadataEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

fun PlantDataEmbedded.toPlant(language: LanguageEnum): Plant {
    return plant.toPlant(
        species = species,
        images = images,
        language = language
    )
}

fun PlantEntity.toPlant(
    language: LanguageEnum,
    species: SpeciesEntity,
    images: List<SpeciesImageWithMetadataEmbedded>
): Plant {
    val isEn = language.isEn
    return Plant(
        id = id,
        species = species.toSpecies(language = language),
        images = images.map { it.toSpeciesImageModel() },
        size = if (isEn) size_c_en else size_c_tr,
        color = if (isEn) color_c_en else color_c_tr,
        floweringTime = if (isEn) flowering_time_c_en else flowering_time_c_tr,
        culturalImportance = if (isEn) cultural_importance_c_en else cultural_importance_c_tr,
        conservationStatus = if (isEn) conservation_status_c_en else conservation_status_c_tr,
        medicinalBenefits = if (isEn) medicinal_benefits_c_en else medicinal_benefits_c_tr,
        potentialHarm = if (isEn) potential_harm_c_en else potential_harm_c_tr,
        physicalCharacteristics = if (isEn) physical_characteristics_en else physical_characteristics_tr,
        naturalHabitat = if (isEn) natural_habitat_en else natural_habitat_tr,
        ecosystem = if (isEn) ecosystem_en else ecosystem_tr,
        growthConditions = if (isEn) growth_conditions_en else growth_conditions_tr,
        developmentProcess = if (isEn) development_process_en else development_process_tr,
        floweringTimeAndCharacteristics = if (isEn) flowering_time_and_characteristics_en else flowering_time_and_characteristics_tr,
        reproductionMethods = if (isEn) reproduction_methods_en else reproduction_methods_tr,
        roleInEcosystem = if (isEn) role_in_ecosystem_en else role_in_ecosystem_tr,
        economicValue = if (isEn) economic_value_en else economic_value_tr,
        environmentalAdaptations = if (isEn) environmental_adaptations_en else environmental_adaptations_tr,
        evolutionaryProcesses = if (isEn) evolutionary_processes_en else evolutionary_processes_tr,
        culturalContext = if (isEn) cultural_context_en else cultural_context_tr,
        historicalUsage = if (isEn) historical_usage_en else historical_usage_tr,
        threatsAndDangers = if (isEn) threats_and_dangers_en else threats_and_dangers_tr,
        conservationEfforts = if (isEn) conservation_efforts_en else conservation_efforts_tr,
        medicinalUsesAndBenefits = if (isEn) medicinal_uses_and_benefits_en else medicinal_uses_and_benefits_tr,
        potentialHarmAndSideEffects = if (isEn) potential_harm_and_side_effects_en else potential_harm_and_side_effects_tr,
        noteworthyCharacteristics = if (isEn) noteworthy_characteristics_en else noteworthy_characteristics_tr,
        surprisingFacts = if (isEn) surprising_facts_en else surprising_facts_tr,
        interestingAndFunFacts = if (isEn) interesting_and_fun_facts_en else interesting_and_fun_facts_tr
    )
}


fun PlantDetailEmbedded.toPlantDetail(
    language: LanguageEnum
): PlantDetail {
    return PlantDetail(
        detail = plant.toPlant(
            species = species,
            images = images,
            language = language
        ),
        phylum = phylum.toPhylumModel(language),
        classModel = classEntity.toClass(language),
        order = order.toOrder(language),
        family = family.toFamily(language),
        genus = genus.toGenus(language),
        species = species.toSpecies(language),
        habitatCategories = habitatCategories.map { it.toHabitatCategory(language) },
        images = images.map { it.toSpeciesImageModel() }
    )
}