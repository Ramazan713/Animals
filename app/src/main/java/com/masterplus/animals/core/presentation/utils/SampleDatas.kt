package com.masterplus.animals.core.presentation.utils

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.GenusModel
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.domain.models.PhylumModel
import com.masterplus.animals.core.domain.models.Plant
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.domain.models.SpeciesImageModel
import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType
import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.core.shared_features.list.domain.models.SelectableListView
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.features.settings.presentation.link_accounts.models.LinkAccountModel
import com.masterplus.animals.features.species_detail.presentation.models.TitleContentModel
import com.masterplus.animals.features.species_detail.presentation.models.TitleSectionModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object SampleDatas {
    const val imageUrl = "https://storage.googleapis.com/animals-ce701.appspot.com/public/images/class/mammalia.jpg?Expires=1724517659&GoogleAccessId=firebase-adminsdk-hprer%40animals-ce701.iam.gserviceaccount.com&Signature=N27yfyDPC7RZzh0Cirowi0Ki%2FisODA%2Bnp9i2dAjR5OEDZV8Cuji0CwMOqhZoQYE9QnD8LOcNI2vR6uJ9ChzUhFrOJ9BmKzroiwzqhRsU1OgFYQwCpE5yi9WFPwpw3Pmlaz%2B1dIr6I3%2BQAfM91y6kMPpFFPwKFwT4H7uS%2FYWEu91x0VLvWH%2FMPXGiYmIcNEQnhigPRT%2BPsjxJyAwoajEOSGgHrXai43%2FtiXoZYYVMbLOOR03BKNcMC4dGC56S6zA05JnjplmZ%2Bn2veofgUGocGOeQ0kL9BngIDjlbv6wS7gxZlHbp4gJEG3UF3HdGi3thfImt3W%2FUthn0dJlpPPcz1g%3D%3D"


    val speciesImageModel = SpeciesImageModel(
        id = 1,
        speciesId = 2,
        name = null,
        imageUrl = imageUrl,
        imagePath = "",
        imageOrder = 1,
        createdAt = "",
        updatedAt = ""
    )

    val imageWithTitleModel1 = ImageWithTitleModel(
        title = "Kartal",
        id = 1,
        imageUrl = imageUrl
    )
    val imageWithTitleModel2 = ImageWithTitleModel(
        title = "Kartal",
        subTitle = "Sub Title",
        id = 2,
        imageUrl = imageUrl
    )

    val categoryData = CategoryData(
        title = "Kartal",
        secondaryTitle = "Sub Title",
        id = 2,
        imageUrl = imageUrl,
        categoryType = CategoryType.Class
    )

    val sampleDestination = SavePointDestination.All(KingdomType.Animals)

    val phylum = PhylumModel(
        id = 2,
        scientificName = "Arthropoda",
        phylum = "Arthropoda",
        kingdomType = KingdomType.Animals,
        imageUrl = "",
        imagePath = ""
    )
    val classModel = ClassModel(
        id = 2,
        scientificName = "Insecta",
        className = "",
        phylumId = 2,
        imageUrl = "",
        imagePath = "",
        kingdomType = KingdomType.Animals,
    )
    val order = OrderModel(
        id = 2,
        scientificName = "Hymenoptera",
        order = "",
        classId = 2,
        imageUrl = "",
        imagePath = "",
        kingdomType = KingdomType.Animals,
    )

    val family = FamilyModel(
        id = 2,
        scientificName = "Apidae",
        family = "",
        orderId = 2,
        imageUrl = "",
        imagePath = "",
        kingdomType = KingdomType.Animals,
    )

    val genus = GenusModel(
        id = 2,
        scientificName = "Apis",
        genus = "",
        familyId = 2,
        kingdomType = KingdomType.Animals,
    )

    val species = SpeciesModel(
        id = 2,
        scientificName = "Apis mellifera",
        introduction="Arılar, dünya genelinde tarımın ve doğal ekosistemlerin sürdürülebilirliği için hayati öneme sahip polinatörlerdir. Bal üretimi ile tanınsalar da, bitkilerin tozlaşmasını sağlayarak birçok meyve ve sebzenin oluşmasında kritik rol oynarlar. Arılar, karmaşık sosyal yapıları ve etkileyici iş bölümleriyle de dikkat çekerler. Özellikle bal arıları, ürettikleri bal ve balmumu ile insanlık için ekonomik değere sahiptir.",
        name ="Bal Arısı",
        recognitionAndInteraction = 4,
        genusId=2,
        kingdomType = KingdomType.Animals
    )

    val habitatCategory = HabitatCategoryModel(
        id = 1,
        habitatCategory = "Karasal"
    )

    val animalImage = SpeciesImageModel(
        id = 1,
        imagePath = "",
        imageUrl = "",
        speciesId = 2,
        imageOrder = 1,
        name = null,
        updatedAt = "",
        createdAt = ""
    )



    val animal = Animal(
        id=2,
        species = species,
        images = listOf(speciesImageModel),
        size = "1.2 meters at the shoulder",
        weight = "150-250 kg (males), 120-182 kg (females)",
        color = "Golden to tawny, with a black mane in males",
        feeding = "Carnivore",
        threats = "Habitat loss, human-wildlife conflict, poaching",
        conservationStatus = "Vulnerable (IUCN)",
        culturalSignificance = "Symbol of strength and royalty",
        physicalCharacteristics = "Lions are large, muscular cats with a well-defined physique. Males typically weigh between 150-250 kg and stand about 1.2 meters at the shoulder, while females are slightly smaller, weighing 120-182 kg. The most distinctive feature of male lions is their mane, which varies in color from blonde to black and serves as a sign of maturity and health.",
        naturalHabitat = "Lions primarily inhabit savanna and grassland ecosystems. They prefer open plains with some dense bush or waterholes where prey is abundant.",
        ecosystem = "African savannas and grasslands.",
        feedingHabits = "Lions are carnivorous and primarily hunt large ungulates such as zebras, wildebeests, and buffaloes. They usually hunt in groups, with females doing most of the hunting and males protecting the pride's territory.",
        socialStructureAndBehaviors = "Lions are unique among big cats for their social structure, living in prides that typically consist of several related females, their offspring, and a small number of adult males. This social organization provides advantages in hunting and territory defense.",
        reproductiveBehaviors = "Females in a pride typically synchronize their reproductive cycles, allowing them to give birth to cubs around the same time. Males do not participate in raising cubs; their role is mainly to protect the pride.",
        developmentOfTheYoung = "Lion cubs are born blind and rely on their mothers for care and protection. They begin to accompany the pride on hunts by the age of 2-3 years and leave the pride to establish their own territories around 2-3 years of age.",
        soundsProduced = "Lions are known for their powerful roars, which can be heard over long distances and are used to communicate between prides and establish territory. They also make a variety of other sounds, including growls, grunts, and purrs, for different social interactions.",
        communicationMethods = "Lions use vocalizations, body language, and scent marking to communicate with each other. Roaring is the most significant method of long-distance communication, while close-range interactions involve physical contact and various vocalizations.",
        threatsAndDangers = "Lions face several threats, including habitat loss due to human encroachment, reduction of prey species, and conflict with humans. In some areas, they are also targeted by poachers and are affected by diseases such as canine distemper.",
        conservationEfforts = "Conservation efforts for lions include the establishment of protected areas, wildlife reserves, and national parks. Efforts are also being made to mitigate human-wildlife conflict and reduce poaching through community-based conservation programs.",
        conservationChallenges = "The main challenges to lion conservation include the fragmentation of their habitats, the decline of prey animals, and ongoing human-wildlife conflict. Ensuring a stable and sufficient population of lions requires effective management of their habitats and conservation programs.",
        culturalAndHistoricalSignificance = "Lions have been revered and symbolized power, courage, and royalty in many cultures throughout history. They are often depicted in art, literature, and mythology and have a significant cultural presence in various societies.",
        economicAndScientificImportance = "Lions are an important species for wildlife tourism, attracting tourists to national parks and reserves. They are also subjects of scientific research, particularly in studies of animal behavior, social structures, and conservation biology.",
        modernDayPerception = "Today, lions are viewed as symbols of wildlife conservation efforts. Despite their popularity, their conservation status remains critical, and they are often featured in campaigns aimed at raising awareness about endangered species.",
        environmentalAdaptations = "Lions are well adapted to life in the savanna with their keen eyesight, powerful muscles, and social hunting strategies. Their golden coats provide camouflage in the grasslands, and their roars are adapted for long-distance communication.",
        evolutionaryProcesses = "The evolution of lions has involved the development of social behavior and dominance hierarchies that are unique among big cats. Over millions of years, lions have adapted to various environments across Africa and parts of Asia, although modern lions are primarily confined to Africa.",
        observedInterestingBehaviors = "Lions are known for their cooperative hunting behavior, which includes strategic planning and teamwork among pride members. Males are also known to engage in 'coalition' behavior, where brothers or related males work together to control and defend a territory.",
        ethologicalInsights = "Ethological studies have revealed that the social structure of lion prides is highly complex, with intricate relationships and roles among members. Research has also shown that female lions often exhibit higher levels of cooperation and social bonding than males.",
        interestingAndFunFacts = "A lion's roar can be heard up to 8 kilometers (5 miles) away. Male lions typically spend around 20 hours a day resting and sleeping, while females are more active in hunting and caring for cubs.",
        comparativeAnalysis = "Compared to other big cats, lions are unique in their social behavior. While most big cats are solitary, lions' pride structure allows them to tackle larger prey and defend their territory more effectively together.",
        roleInEcosystem = "As apex predators, lions play a crucial role in maintaining the balance of their ecosystems by controlling the population of herbivores. This helps to prevent overgrazing and supports the health of their habitat."
    )

    val plant = Plant(
        id = 5,
        species = species,
        images = listOf(speciesImageModel),
        size = "1-3 meters in height",
        color = "Pale pink flowers, red hips",
        floweringTime = "May to June",
        culturalImportance = "Valued for fragrance and beauty in gardens",
        conservationStatus = "Not endangered, may be invasive",
        medicinalBenefits = "High in vitamin C, immune support",
        potentialHarm = "Digestive discomfort from excessive consumption",
        physicalCharacteristics = "Thorny stems, soft green leaves with apple-like scent, pink to light red flowers, red hips",
        naturalHabitat = "Temperate regions including woodland edges, hedgerows, and scrubland",
        ecosystem = "Temperate, terrestrial",
        growthConditions = "Grows best in well-drained soils and full sun but can tolerate a range of soil types",
        developmentProcess = "Develops thorny stems and fragrant leaves; flowering occurs in late spring to early summer",
        floweringTimeAndCharacteristics = "Flowers from May to June, producing fragrant, pale pink to light red blossoms",
        reproductionMethods = "Reproduces sexually through seeds and asexually through suckering and layering",
        roleInEcosystem = "Provides food and habitat for various wildlife, including pollinators and seed dispersers",
        economicValue = "Used in herbal medicine and traditional remedies; valued for ornamental qualities",
        environmentalAdaptations = "Adapted to drought and poor soils; thorny stems deter herbivores",
        evolutionaryProcesses = "Adapted to temperate regions with thorny stems, fragrant leaves, and seed-dispersing hips",
        culturalContext = "Symbolizes love and friendship; featured in folklore",
        historicalUsage = "Historically used in traditional medicine for its antiseptic and vitamin-rich properties",
        threatsAndDangers = "Not endangered but can be invasive, outcompeting native flora",
        conservationEfforts = "Regular management recommended in areas where it is invasive",
        medicinalUsesAndBenefits = "Hips are high in vitamin C and used to boost the immune system",
        potentialHarmAndSideEffects = "May cause digestive discomfort if consumed in excess",
        noteworthyCharacteristics = "Apple-scented leaves and attractive red hips, especially in autumn",
        surprisingFacts = "Red hips remain on the plant even in winter, providing food for wildlife",
        interestingAndFunFacts = "A single cup of rosehip tea can contain 10 times more vitamin C than an orange"
    )

    val animalDetail = AnimalDetail(
        detail = animal,
        phylum = phylum,
        classModel = classModel,
        order = order,
        family = family,
        genus = genus,
        species = species,
        habitatCategories = listOf(habitatCategory),
        images = listOf(animalImage, animalImage, animalImage)
    )

    val titleSectionModel = TitleSectionModel(
        sectionTitle = "Yaşam Alanı",
        titleContents = listOf(
            TitleContentModel(
                title = "Doğal Habitat",
                content = animal.naturalHabitat ?: ""
            ),
            TitleContentModel(
                title = "Ekosistem",
                content = animal.ecosystem ?: ""
            )
        ),
        imageUrl = imageUrl
    )

    fun generateSavePoint(
        id: Int = 1,
        title: String = "Title $id",
        itemPosIndex: Int = 5,
        savePointDestination: SavePointDestination = SavePointDestination.ListType(1),
        modifiedDate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    ): SavePoint {
        return SavePoint(id, title, SavePointContentType.Content,savePointDestination, KingdomType.Animals, SavePointSaveMode.Manuel, itemPosIndex, modifiedDate,
            imageUrl,null)
    }

    fun generateLinkAccountModel(
        providerType: AuthProviderType = AuthProviderType.Email,
        isConnected: Boolean = true,
        title: UiText = providerType.title,
        info: UiText? = null
    ): LinkAccountModel {
        return LinkAccountModel(
            providerType = providerType,
            isConnected = isConnected,
            title = title,
            info = info
        )
    }

    fun generateSpeciesDetail(
        id: Int = 1,
        name: String = "Bal Arısi",
        scientificName: String = "Apis mellifera",
        introduction: String = "Arılar, dünya genelinde tarımın ve doğal ekosistemlerin sürdürülebilirliği için hayati öneme sahip polinatörlerdir. Bal üretimi ile tanınsalar da, bitkilerin tozlaşmasını sağlayarak birçok meyve ve sebzenin oluşmasında kritik rol oynarlar. Arılar, karmaşık sosyal yapıları ve etkileyici iş bölümleriyle de dikkat çekerler. Özellikle bal arıları, ürettikleri bal ve balmumu ile insanlık için ekonomik değere sahiptir.",
        imageUrls: List<SpeciesImageModel> = listOf(speciesImageModel),
        isFavorited: Boolean = false,
        isListSelected: Boolean = false,
        habitatCategoryId: Int = 1
    ): SpeciesListDetail{
        return SpeciesListDetail(
            id = id,
            name = name,
            scientificName = scientificName,
            introduction = introduction,
            images = imageUrls,
            isFavorited = isFavorited,
            isListSelected = isListSelected,
            habitatCategoryIds = listOf(habitatCategoryId),
            recognitionAndInteraction = 1,
            genusId = 1
        )
    }

    fun generateListView(
        id: Int = 1,
        name: String = "list item $id",
        isRemovable: Boolean = false,
        isArchive: Boolean = false,
        listPos: Int = 1,
        contentMaxPos: Int = 1,
        itemCounts: Int = 2
    ): ListView{
        return ListView(id, name, isRemovable, isArchive, listPos, contentMaxPos, itemCounts)
    }

    fun generateSelectableListView(
        listView: ListView = generateListView(),
        isSelected: Boolean = false
    ): SelectableListView{
        return SelectableListView(listView, isSelected)
    }


    val selectableListViewArr = listOf(
        generateSelectableListView(generateListView(id = 1)),
        generateSelectableListView(generateListView(id = 2)),
        generateSelectableListView(generateListView(id = 3))
    )

    val listViewFavorite = generateListView(id = 1, isRemovable = false)
    val listView = generateListView(id = 2, isRemovable = true)
}