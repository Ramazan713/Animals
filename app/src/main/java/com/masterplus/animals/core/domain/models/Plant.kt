package com.masterplus.animals.core.domain.models

data class Plant(
    override val id: Int?,
    override val species: SpeciesModel,
    override val images: List<SpeciesImageModel>,
    val size: String?,
    val color: String?,
    val floweringTime: String?,
    val culturalImportance: String?,
    val conservationStatus: String?,
    val medicinalBenefits: String?,
    val potentialHarm: String?,
    val physicalCharacteristics: String?,
    val naturalHabitat: String?,
    val ecosystem: String?,
    val growthConditions: String?,
    val developmentProcess: String?,
    val floweringTimeAndCharacteristics: String?,
    val reproductionMethods: String?,
    val roleInEcosystem: String?,
    val economicValue: String?,
    val environmentalAdaptations: String?,
    val evolutionaryProcesses: String?,
    val culturalContext: String?,
    val historicalUsage: String?,
    val threatsAndDangers: String?,
    val conservationEfforts: String?,
    val medicinalUsesAndBenefits: String?,
    val potentialHarmAndSideEffects: String?,
    val noteworthyCharacteristics: String?,
    val surprisingFacts: String?,
    val interestingAndFunFacts: String?
): ISpeciesType


data class PlantDetail(
    override val detail: Plant,
    override val phylum: PhylumModel,
    override val classModel: ClassModel,
    override val order: OrderModel,
    override val family: FamilyModel,
    override val genus: GenusModel,
    override val species: SpeciesModel,
    override val habitatCategories: List<HabitatCategoryModel>,
    override val images: List<SpeciesImageModel>,
    override val isFavorited: Boolean,
    override val isListSelected: Boolean
): SpeciesDetail
