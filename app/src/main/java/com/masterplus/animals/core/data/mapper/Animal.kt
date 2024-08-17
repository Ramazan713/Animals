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
        images = images.map { it.toAnimalImage() },
        physicalCharacteristics = if(isEn) physical_characteristics_en else physical_characteristics_tr,
        naturalHabitat = if(isEn) natural_habitat_en else natural_habitat_tr,
        ecosystem = if(isEn) ecosystem_en else ecosystem_tr,
        feedingHabits = if(isEn) feeding_habits_en else feeding_habits_tr,
        socialStructure = if(isEn) social_structure_en else social_structure_tr,
        reproductiveBehaviors = if(isEn) reproductive_behaviors_en else reproductive_behaviors_tr,
        developmentStages = if(isEn) development_stages_en else development_stages_tr,
        soundsProduced = if(isEn) sounds_produced_en else sounds_produced_tr,
        communicationMethods = if(isEn) communication_methods_en else communication_methods_tr,
        threats = if(isEn) threats_en else threats_tr,
        conservationEfforts = if(isEn) conservation_efforts_en else conservation_efforts_tr,
        culturalSignificance = if(isEn) cultural_significance_en else cultural_significance_tr,
        economicImportance = if(isEn) economic_importance_en else economic_importance_tr,
        environmentalAdaptations = if(isEn) environmental_adaptations_en else environmental_adaptations_tr,
        evolutionaryProcesses = if(isEn) evolutionary_processes_en else evolutionary_processes_tr,
        interestingBehaviors = if(isEn) interesting_behaviors_en else interesting_behaviors_tr,
        funFacts = if(isEn) fun_facts_en else fun_facts_tr,
        size = if(isEn) size_en else size_tr,
        weight = if(isEn) weight_en else weight_tr,
        color = if(isEn) color_en else color_tr,
        habitat = if(isEn) habitat_en else habitat_tr,
        ecosystemCategory = if(isEn) ecosystem_category_en else ecosystem_category_tr,
        feeding = if(isEn) feeding_en else feeding_tr,
        socialStructureSimple = if(isEn) social_structure_simple_en else social_structure_simple_tr,
        reproductiveSimple = if(isEn) reproductive_simple_en else reproductive_simple_tr,
        developmentSimple = if(isEn) development_simple_en else development_simple_tr,
        sounds = if(isEn) sounds_en else sounds_tr,
        communication = if(isEn) communication_en else communication_tr,
        threatsSimple = if(isEn) threats_simple_en else threats_simple_tr,
        conservationStatus = if(isEn) conservation_status_en else conservation_status_tr,
        culturalSimple = if(isEn) cultural_simple_en else cultural_simple_tr,
        economicSimple = if(isEn) economic_simple_en else economic_simple_tr,
        adaptation = if(isEn) adaptation_en else adaptation_tr,
        evolution = if(isEn) evolution_en else evolution_tr,
        conservationChallenges = if(isEn) conservation_challenges_en else conservation_challenges_tr,
        comparativeAnalysis = if(isEn) comparative_analysis_en else comparative_analysis_tr,
        ethologicalInsights = if(isEn) ethological_insights_en else ethological_insights_tr,
        modernDayPerception = if(isEn) modern_day_perception_en else modern_day_perception_tr
    )
}