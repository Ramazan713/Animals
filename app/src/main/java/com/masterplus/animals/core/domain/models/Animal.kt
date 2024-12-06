package com.masterplus.animals.core.domain.models

data class Animal(
    override val id: Int?,
    override val species: SpeciesModel,
    override val images: List<SpeciesImageModel>,
    val size: String?,
    val weight: String?,
    val color: String?,
    val feeding: String?,
    val threats: String?,
    val conservationStatus: String?,
    val culturalSignificance: String?,
    val physicalCharacteristics: String?,
    val naturalHabitat: String?,
    val ecosystem: String?,
    val feedingHabits: String?,
    val socialStructureAndBehaviors: String?,
    val reproductiveBehaviors: String?,
    val developmentOfTheYoung: String?,
    val soundsProduced: String?,
    val communicationMethods: String?,
    val threatsAndDangers: String?,
    val conservationEfforts: String?,
    val conservationChallenges: String?,
    val culturalAndHistoricalSignificance: String?,
    val economicAndScientificImportance: String?,
    val modernDayPerception: String?,
    val environmentalAdaptations: String?,
    val evolutionaryProcesses: String?,
    val observedInterestingBehaviors: String?,
    val ethologicalInsights: String?,
    val interestingAndFunFacts: String?,
    val comparativeAnalysis: String?,
    val roleInEcosystem: String?
): ISpeciesType


data class AnimalDetail(
    override val detail: Animal,
    override val phylum: PhylumModel,
    override val classModel: ClassModel,
    override val order: OrderModel,
    override val family: FamilyModel,
    override val genus: GenusModel,
    override val species: SpeciesModel,
    override val habitatCategories: List<HabitatCategoryModel>,
    override val images: List<SpeciesImageModel>
): SpeciesDetail
