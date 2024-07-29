package com.masterplus.animals.core.presentation.utils

import com.masterplus.animals.core.domain.models.AnimalData
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

object SampleDatas {
    const val imageUrl = "https://storage.googleapis.com/animals-ce701.appspot.com/public/images/class/mammalia.jpg?Expires=1724517659&GoogleAccessId=firebase-adminsdk-hprer%40animals-ce701.iam.gserviceaccount.com&Signature=N27yfyDPC7RZzh0Cirowi0Ki%2FisODA%2Bnp9i2dAjR5OEDZV8Cuji0CwMOqhZoQYE9QnD8LOcNI2vR6uJ9ChzUhFrOJ9BmKzroiwzqhRsU1OgFYQwCpE5yi9WFPwpw3Pmlaz%2B1dIr6I3%2BQAfM91y6kMPpFFPwKFwT4H7uS%2FYWEu91x0VLvWH%2FMPXGiYmIcNEQnhigPRT%2BPsjxJyAwoajEOSGgHrXai43%2FtiXoZYYVMbLOOR03BKNcMC4dGC56S6zA05JnjplmZ%2Bn2veofgUGocGOeQ0kL9BngIDjlbv6wS7gxZlHbp4gJEG3UF3HdGi3thfImt3W%2FUthn0dJlpPPcz1g%3D%3D"

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

    val animalData = AnimalData(
        id = 1,
        name = "Bal Arısi",
        scientificName = "Apis mellifera",
        introduction = "Arılar, dünya genelinde tarımın ve doğal ekosistemlerin sürdürülebilirliği için hayati öneme sahip polinatörlerdir. Bal üretimi ile tanınsalar da, bitkilerin tozlaşmasını sağlayarak birçok meyve ve sebzenin oluşmasında kritik rol oynarlar. Arılar, karmaşık sosyal yapıları ve etkileyici iş bölümleriyle de dikkat çekerler. Özellikle bal arıları, ürettikleri bal ve balmumu ile insanlık için ekonomik değere sahiptir.",
        imageUrls = listOf(imageUrl)
    )
}