package com.masterplus.animals.features.species_detail.presentation.mapper

import com.masterplus.animals.core.domain.models.Plant
import com.masterplus.animals.core.domain.models.PlantDetail
import com.masterplus.animals.features.species_detail.presentation.models.TitleContentModel
import com.masterplus.animals.features.species_detail.presentation.models.TitleSectionModel


fun Plant.toTitleSections(imageUrls: List<String> = emptyList()): List<TitleSectionModel>{
    val titleSections = mutableListOf<TitleSectionModel>()

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Doğal Yaşam ve Özellikler",
            titleContents = listOf(
                TitleContentModel(
                    title = "Fiziksel Özellikler",
                    content = physicalCharacteristics
                ),
                TitleContentModel(
                    title = "Büyüme Koşulları",
                    content = growthConditions
                ),
                TitleContentModel(
                    title = "Çevresel Uyarlamalar",
                    content = environmentalAdaptations
                ),
            ),
            imageUrl = imageUrls.getOrNull(0)
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
                    title = "Şaşırtıcı Gerçekler",
                    content = surprisingFacts
                ),
                TitleContentModel(
                    title = "Dikkate Değer Özellikler",
                    content = noteworthyCharacteristics
                )
            ),
            imageUrl = imageUrls.getOrNull(3)
        )
    )


    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Çiçeklenme ve Üreme",
            titleContents = listOf(
                TitleContentModel(
                    title = "Çiçeklenme Zamanı ve Özellikleri",
                    content = floweringTimeAndCharacteristics
                ),
                TitleContentModel(
                    title = "Üreme Yöntemleri",
                    content = reproductionMethods
                ),
                TitleContentModel(
                    title = "Gelişim Süreci",
                    content = developmentProcess
                )
            ),
            imageUrl = imageUrls.getOrNull(1)
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
            imageUrl = imageUrls.getOrNull(4)
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Koruma Çalışmaları",
            titleContents = listOf(
                TitleContentModel(
                    title = "Koruma Çabaları",
                    content = conservationEfforts
                )
            ),
            imageUrl = imageUrls.getOrNull(2)
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Tıbbi ve Ekonomik Değer",
            titleContents = listOf(
                TitleContentModel(
                    title = "Tıbbi Kullanımlar ve Faydalar",
                    content = medicinalUsesAndBenefits
                ),
                TitleContentModel(
                    title = "Potansiyel Zararlar ve Yan Etkiler",
                    content = potentialHarmAndSideEffects
                ),
                TitleContentModel(
                    title = "Ekonomik Değeri",
                    content = economicValue
                )
            ),
            imageUrl = imageUrls.getOrNull(5)
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Kültürel ve Tarihsel Bağlam",
            titleContents = listOf(
                TitleContentModel(
                    title = "Kültürel Bağlam",
                    content = culturalContext
                ),
                TitleContentModel(
                    title = "Tarihsel Kullanım",
                    content = historicalUsage
                )
            ),
            imageUrl = imageUrls.getOrNull(6)
        )
    )

    titleSections.add(
        TitleSectionModel(
            sectionTitle = "Evrim ve Bilimsel Analiz",
            titleContents = listOf(
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




fun PlantDetail.toFeatureSection2(): List<TitleContentModel>{
    val titleContents = mutableListOf<TitleContentModel>()

    titleContents.add(
        TitleContentModel(
            title = "Boyut",
            content = detail.size
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



fun Plant.toFeatureSection3(): List<TitleContentModel>{
    val titleContents = mutableListOf<TitleContentModel>()

    titleContents.add(
        TitleContentModel(
            title = "Koruma Durumu",
            content = conservationStatus
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Tıbbi Yararları",
            content = medicinalBenefits
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Çiçeklenme Zamanı",
            content = floweringTime
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Kültürel Önemi",
            content = culturalImportance
        ),
    )

    titleContents.add(
        TitleContentModel(
            title = "Doğal Yaşam Alanı",
            content = naturalHabitat
        ),
    )





    return titleContents.mapNotNull { titleContent ->
        if(titleContent.content.isNullOrBlank()) return@mapNotNull null
        titleContent
    }
}