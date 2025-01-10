package com.masterplus.animals.core.data.dtos

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class ClassDto(
    val class_en: String = "",
    val class_tr: String = "",
    val id: Int = 0,
    val order_key: Int = 0,
    val image: ImageDto? = null,
    val kingdom_id: Int = 0,
    val phylum_id: Int = 0,
    val scientific_name: String = ""
)

@Keep
@Serializable
data class FamilyDto(
    val family_en: String = "",
    val family_tr: String = "",
    val id: Int = 0,
    val order_key: Int = 0,
    val kingdom_id: Int = 0,
    val order_id: Int = 0,
    val scientific_name: String = "",
    val image: ImageDto? = null,
)

@Keep
@Serializable
data class HabitatCategoryDto(
    val habitat_category_en: String = "",
    val habitat_category_tr: String = "",
    val id: Int = 0,
    val image: ImageDto? = null,
    val kingdom_ids: List<Int> = emptyList()
)

@Keep
@Serializable
data class PhylumDto(
    val id: Int = 0,
    val kingdom_id: Int = 0,
    val order_key: Int = 0,
    val phylum_en: String = "",
    val phylum_tr: String = "",
    val scientific_name: String = "",
    val image: ImageDto? = null,
)

@Keep
@Serializable
data class OrderDto(
    val class_id: Int = 0,
    val id: Int = 0,
    val order_key: Int = 0,
    val kingdom_id: Int = 0,
    val order_en: String = "",
    val order_tr: String = "",
    val scientific_name: String = "",
    val image: ImageDto? = null,
)



@Keep
@Serializable
data class ImageDto(
    val id: Int = 0,
    val image_path: String = "",
    val image_url: String = "",
    val metadata: MetadataDto? = null,
    val image_order: Int? = null
)

@Keep
@Serializable
data class MetadataDto(
    val artist_name: String = "",
    val date_time_original: String = "",
    val description_url: String = "",
    val image_description: String = "",
    val license_short_name: String = "",
    val license_url: String = "",
    val usage_terms: String = ""
)