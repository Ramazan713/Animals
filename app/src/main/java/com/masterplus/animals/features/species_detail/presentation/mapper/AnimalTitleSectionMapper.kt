package com.masterplus.animals.features.species_detail.presentation.mapper

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.features.species_detail.presentation.models.TitleContentModel
import com.masterplus.animals.features.species_detail.presentation.models.TitleSectionModel


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
                    title = "Doğal Yaşam Alanı",
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
            )
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "İlginç Davranışlar",
            titleContents = listOf(
                TitleContentModel(
                    title = "Etolojik Bilgiler",
                    content = ethologicalInsights
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
                    title = "Karşılaştırmalı Analiz",
                    content = comparativeAnalysis
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
                    title = "Günümüz Algısı",
                    content = modernDayPerception
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

    return titleSections.mapNotNull { titleSection ->
        val newTitleContents = titleSection.titleContents.mapNotNull { titleContent ->
            if(titleContent.content.isNullOrBlank()) null
            else titleContent
        }
        if(newTitleContents.isEmpty()) return@mapNotNull null
        titleSection.copy(
            titleContents = newTitleContents
        )
    }
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

    return titleContents.mapNotNull { titleContent ->
        if(titleContent.content.isNullOrBlank()) return@mapNotNull null
        titleContent
    }
}

fun AnimalDetail.toFeatureSection2(): List<TitleContentModel>{
    val titleContents = mutableListOf<TitleContentModel>()

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
    return titleContents.mapNotNull { titleContent ->
        if(titleContent.content.isNullOrBlank()) return@mapNotNull null
        titleContent
    }
}



fun Animal.toFeatureSection3(): List<TitleContentModel>{
    val titleContents = mutableListOf<TitleContentModel>()

    titleContents.add(
        TitleContentModel(
            title = "Beslenme",
            content = feeding
        )
    )


    titleContents.add(
        TitleContentModel(
            title = "Koruma Durumu",
            content = conservationStatus
        )
    )

    return titleContents.mapNotNull { titleContent ->
        if(titleContent.content.isNullOrBlank()) return@mapNotNull null
        titleContent
    }
}