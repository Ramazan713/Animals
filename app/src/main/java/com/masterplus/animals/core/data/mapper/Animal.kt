package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalWithImagesEntity

fun AnimalWithImagesEntity.toAnimal(): Animal {
    return with(animal){
        Animal(
            id = id,
            introduction = introduction_tr,
            scientificName = species.scientific_name,
            name = name_tr,
            physicalCharacteristics = physical_characteristics_tr,
            naturalHabitat = natural_habitat_tr,
            ecosystem = ecosystem_tr,
            feedingHabits = feeding_habits_tr,
            socialStructure = social_structure_tr,
            reproductiveBehaviors = reproductive_behaviors_tr,
            developmentStages = development_stages_tr,
            soundsProduced = sounds_produced_tr,
            communicationMethods = communication_methods_tr,
            threats = threats_tr,
            conservationEfforts = conservation_efforts_tr,
            culturalSignificance = cultural_significance_tr,
            economicImportance = economic_importance_tr,
            environmentalAdaptations = environmental_adaptations_tr,
            evolutionaryProcesses = evolutionary_processes_tr,
            interestingBehaviors = interesting_behaviors_tr,
            unknownFeatures = unknown_features_tr,
            funFacts = fun_facts_tr,
            habitatCategoryId = habitat_category_id,
            phylumId = phylum_id,
            classId = class_id,
            orderId = order_id,
            familyId = family_id,
            genusId = genus_id,
            speciesId = species_id,
            size = size_tr,
            weight = weight_tr,
            color = color_tr,
            habitat = habitat_tr,
            ecosystemCategory = ecosystem_category_tr,
            feeding = feeding_tr,
            socialStructureSimple = social_structure_simple_tr,
            reproductiveSimple = reproductive_simple_tr,
            developmentSimple = development_simple_tr,
            sounds = sounds_tr,
            communication = communication_tr,
            threatsSimple = threats_simple_tr,
            conservationStatus = conservation_status_tr,
            culturalSimple = cultural_simple_tr,
            economicSimple = economic_simple_tr,
            adaptation = adaptation_tr,
            evolution = evolution_tr,
            recognitionAndInteraction = recognition_and_interaction,
            imageUrls = images.map { it.image_url }
        )
    }
}