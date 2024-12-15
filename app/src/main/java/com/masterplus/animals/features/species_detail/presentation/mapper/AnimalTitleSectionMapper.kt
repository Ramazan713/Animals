package com.masterplus.animals.features.species_detail.presentation.mapper

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.features.species_detail.presentation.models.TitleContentModel
import com.masterplus.animals.features.species_detail.presentation.models.TitleSectionModel


fun Animal.toTitleSections(images: List<ImageWithMetadata> = emptyList()): List<TitleSectionModel>{
    val titleSections = mutableListOf<TitleSectionModel>()

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Doğal Yaşam ve Özellikler",
            titleContents = listOf(
                TitleContentModel(
                    title = "Doğal Yaşam Alanı",
                    content = naturalHabitat
                ),
                TitleContentModel(
                    title = "Fiziksel Özellikler",
                    content = physicalCharacteristics
                ),
                TitleContentModel(
                    title = "Beslenme Alışkanlıkları",
                    content = feedingHabits
                ),
                TitleContentModel(
                    title = "Çevresel Uyarlamalar",
                    content = environmentalAdaptations
                )
            ),
            image = images.getOrNull(0)
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "İlginç ve Eğlenceli Bilgiler",
            titleContents = listOf(
                TitleContentModel(
                    title = "Eğlenceli ve İlginç Bilgiler",
                    content = interestingAndFunFacts
                ),
                TitleContentModel(
                    title = "Gözlemlenen İlginç Davranışlar",
                    content = observedInterestingBehaviors
                ),
                TitleContentModel(
                    title = "Etolojik İçgörüler",
                    content = ethologicalInsights
                )
            ),
            image = images.getOrNull(3)
        )
    )



    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Sosyal Yapı ve Davranışlar",
            titleContents = listOf(
                TitleContentModel(
                    title = "Sosyal Yapı ve Davranışlar",
                    content = socialStructureAndBehaviors
                ),
                TitleContentModel(
                    title = "Üreme Davranışları",
                    content = reproductiveBehaviors
                ),
                TitleContentModel(
                    title = "Gençlerin Gelişimi",
                    content = developmentOfTheYoung
                ),
                TitleContentModel(
                    title = "Üretilen Sesler",
                    content = soundsProduced
                ),
                TitleContentModel(
                    title = "İletişim Yöntemleri",
                    content = communicationMethods
                )
            ),
            image = images.getOrNull(1)
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Ekoloji ve Çevre",
            titleContents = listOf(
                TitleContentModel(
                    title = "Ekosistem",
                    content = ecosystem
                ),
                TitleContentModel(
                    title = "Ekosistemdeki Rolü",
                    content = roleInEcosystem
                ),
                TitleContentModel(
                    title = "Tehditler ve Tehlikeler",
                    content = threatsAndDangers
                )
            ),
            image = images.getOrNull(4)
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Koruma Çalışmaları",
            titleContents = listOf(
                TitleContentModel(
                    title = "Koruma Çabaları",
                    content = conservationEfforts
                ),
                TitleContentModel(
                    title = "Koruma Zorlukları",
                    content = conservationChallenges
                )
            ),
            image = images.getOrNull(2)
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Kültürel ve Ekonomik Önemi",
            titleContents = listOf(
                TitleContentModel(
                    title = "Kültürel ve Tarihsel Anlamı",
                    content = culturalAndHistoricalSignificance
                ),
                TitleContentModel(
                    title = "Ekonomik ve Bilimsel Değer",
                    content = economicAndScientificImportance
                ),
                TitleContentModel(
                    title = "Modern Gün Algısı",
                    content = modernDayPerception
                )
            ),
            image = images.getOrNull(5)
        ),

    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Evrim ve Bilimsel Analiz",
            titleContents = listOf(
                TitleContentModel(
                    title = "Evrimsel Süreçler",
                    content = evolutionaryProcesses
                ),
                TitleContentModel(
                    title = "Karşılaştırmalı Analiz",
                    content = comparativeAnalysis
                )
            ),
            image = images.getOrNull(6)
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


fun AnimalDetail.toFeatureSection2(): List<TitleContentModel>{
    val titleContents = mutableListOf<TitleContentModel>()

    titleContents.add(
        TitleContentModel(
            title = "Boyut",
            content = detail.size
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Ağırlık",
            content = detail.weight
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Renk",
            content = detail.color
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

    titleContents.add(
        TitleContentModel(
            title = "Kültürel Anlamı",
            content = culturalSignificance
        ),
    )

    return titleContents.mapNotNull { titleContent ->
        if(titleContent.content.isNullOrBlank()) return@mapNotNull null
        titleContent
    }
}