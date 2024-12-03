package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.shared_features.database.entity.AnimalEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDataEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum


fun AnimalDataEmbedded.toAnimal(language: LanguageEnum): Animal{
    return animal.toAnimal(
        species = species,
        images = images,
        language = language
    )
}

fun AnimalEntity.toAnimal(
    language: LanguageEnum,
    species: SpeciesEntity,
    images: List<SpeciesImageEntity>
): Animal{
    val isEn = language.isEn
    return Animal(
        id = id,
        species = species.toSpecies(language = language),
        images = images.map { it.toSpeciesImage() },
        size = if (isEn) size_c_en else size_c_tr,
        weight = if (isEn) weight_c_en else weight_c_tr,
        color = if (isEn) color_c_en else color_c_tr,
        feeding = if (isEn) feeding_c_en else feeding_c_tr,
        threats = if (isEn) threats_c_en else threats_c_tr,
        conservationStatus = if (isEn) conservation_status_c_en else conservation_status_c_tr,
        culturalSignificance = if (isEn) cultural_significance_c_en else cultural_significance_c_tr,
        physicalCharacteristics = if (isEn) physical_characteristics_en else physical_characteristics_tr,
        naturalHabitat = if (isEn) natural_habitat_en else natural_habitat_tr,
        ecosystem = if (isEn) ecosystem_en else ecosystem_tr,
        feedingHabits = if (isEn) feeding_habits_en else feeding_habits_tr,
        socialStructureAndBehaviors = if (isEn) social_structure_and_behaviors_en else social_structure_and_behaviors_tr,
        reproductiveBehaviors = if (isEn) reproductive_behaviors_en else reproductive_behaviors_tr,
        developmentOfTheYoung = if (isEn) development_of_the_young_en else development_of_the_young_tr,
        soundsProduced = if (isEn) sounds_produced_en else sounds_produced_tr,
        communicationMethods = if (isEn) communication_methods_en else communication_methods_tr,
        threatsAndDangers = if (isEn) threats_and_dangers_en else threats_and_dangers_tr,
        conservationEfforts = if (isEn) conservation_efforts_en else conservation_efforts_tr,
        conservationChallenges = if (isEn) conservation_challenges_en else conservation_challenges_tr,
        culturalAndHistoricalSignificance = if (isEn) cultural_and_historical_significance_en else cultural_and_historical_significance_tr,
        economicAndScientificImportance = if (isEn) economic_and_scientific_importance_en else economic_and_scientific_importance_tr,
        modernDayPerception = if (isEn) modern_day_perception_en else modern_day_perception_tr,
        environmentalAdaptations = if (isEn) environmental_adaptations_en else environmental_adaptations_tr,
        evolutionaryProcesses = if (isEn) evolutionary_processes_en else evolutionary_processes_tr,
        observedInterestingBehaviors = if (isEn) observed_interesting_behaviors_en else observed_interesting_behaviors_tr,
        ethologicalInsights = if (isEn) ethological_insights_en else ethological_insights_tr,
        interestingAndFunFacts = if (isEn) interesting_and_fun_facts_en else interesting_and_fun_facts_tr,
        comparativeAnalysis = if (isEn) comparative_analysis_en else comparative_analysis_tr,
        roleInEcosystem = if (isEn) role_in_ecosystem_en else role_in_ecosystem_tr,
    )
}