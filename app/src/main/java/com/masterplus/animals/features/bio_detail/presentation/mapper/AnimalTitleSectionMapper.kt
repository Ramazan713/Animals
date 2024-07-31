package com.masterplus.animals.features.bio_detail.presentation.mapper

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.features.bio_detail.presentation.models.TitleContentModel
import com.masterplus.animals.features.bio_detail.presentation.models.TitleSectionModel


fun Animal.toTitleSections(imageUrls: List<String> = emptyList()): List<TitleSectionModel>{
    val titleSections = mutableListOf<TitleSectionModel>()

    titleSections.add(
        TitleSectionModel(
            sectionTitle = null,
            titleContents = listOf(
                TitleContentModel(
                    title = "Fiziksel Özellikler",
                    content = physicalCharacteristics
                )
            ),
            imageUrl = imageUrls.getOrNull(0)
        )
    )


    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Yaşam Alanı",
            titleContents = listOf(
                TitleContentModel(
                    title = "Doğal Habitat",
                    content = naturalHabitat
                ),
                TitleContentModel(
                    title = "Ekosistem",
                    content = ecosystem
                )
            )
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Davranış ve Ekoloji",
            titleContents = listOf(
                TitleContentModel(
                    title = "Beslenme Alışkanlıkları",
                    content = feedingHabits
                ),
                TitleContentModel(
                    title = "Sosyal Yapı ve Davranışlar",
                    content = socialStructure
                )
            )
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "İlginç Davranışlar",
            titleContents = listOf(
                TitleContentModel(
                    title = "Gözlemlenen İlginç Davranışlar",
                    content = interestingBehaviors
                ),
                TitleContentModel(
                    title = "Bilinmeyen veya Az Bilinen Özellikler",
                    content = unknownFeatures
                )
            ),
            imageUrl = imageUrls.getOrNull(1)
        )
    )


    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Eğlenceli Gerçekler",
            titleContents = listOf(
                TitleContentModel(
                    title = "İlginç ve eğlenceli bilgiler",
                    content = funFacts
                ),
            )
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Üreme ve Gelişim",
            titleContents = listOf(
                TitleContentModel(
                    title = "Üreme Davranışları",
                    content = reproductiveBehaviors
                ),
                TitleContentModel(
                    title = "Yavruların Gelişimi",
                    content = developmentStages
                )
            )
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Ses ve İletişim",
            titleContents = listOf(
                TitleContentModel(
                    title = "Çıkardığı Sesler",
                    content = soundsProduced
                ),
                TitleContentModel(
                    title = "İletişim Şekilleri",
                    content = communicationMethods
                )
            ),
            imageUrl = imageUrls.getOrNull(2)
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Korunma Durumu",
            titleContents = listOf(
                TitleContentModel(
                    title = "Tehditler ve Tehlikeler",
                    content = threats
                ),
                TitleContentModel(
                    title = "Koruma Çabaları",
                    content = conservationEfforts
                )
            )
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "İnsanlarla İlişkiler",
            titleContents = listOf(
                TitleContentModel(
                    title = "Kültürel ve Tarihsel Önemi",
                    content = culturalSignificance
                ),
                TitleContentModel(
                    title = "Ekonomik ve Bilimsel Önemi",
                    content = economicImportance
                )
            )
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Adaptasyon ve Evrim",
            titleContents = listOf(
                TitleContentModel(
                    title = "Çevresel Adaptasyonlar",
                    content = environmentalAdaptations
                ),
                TitleContentModel(
                    title = "Evrimsel Süreçler",
                    content = evolutionaryProcesses
                )
            )
        )
    )


    return titleSections
}



fun AnimalDetail.toScientificNomenclatureSection(): List<TitleContentModel>{
    val titleContents = mutableListOf<TitleContentModel>()

    titleContents.add(
        TitleContentModel(
            title = "Şube",
            content = phylum.scientificName
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Sınıf",
            content = classModel.scientificName
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Takım",
            content = order.scientificName
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Familya",
            content = family.scientificName
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Cins",
            content = genus.scientificName
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Tür",
            content = species.scientificName
        )
    )

    return titleContents
}

fun AnimalDetail.toFeatureSection2(): List<TitleContentModel>{
    val titleContents = mutableListOf<TitleContentModel>()

    titleContents.add(
        TitleContentModel(
            title = "Yaşam Alanı",
            content = habitatCategory.habitatCategory
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Boyut",
            content = animal.size
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Ağırlık",
            content = animal.weight
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Renk",
            content = animal.color
        )
    )
    return titleContents
}



fun Animal.toFeatureSection3(): List<TitleContentModel>{
    val titleContents = mutableListOf<TitleContentModel>()

    titleContents.add(
        TitleContentModel(
            title = "Habitat",
            content = habitat
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Ekosistem",
            content = ecosystemCategory
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Beslenme",
            content = feeding
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Sosyal Yapı",
            content = socialStructureSimple
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Üreme Davranışları",
            content = reproductiveSimple
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Yavruların Gelişimi",
            content = developmentSimple
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Koruma Durumu",
            content = conservationStatus
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Kültürel Önemi",
            content = culturalSimple
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Ekonomik Önemi",
            content = economicSimple
        )
    )
    return titleContents
}