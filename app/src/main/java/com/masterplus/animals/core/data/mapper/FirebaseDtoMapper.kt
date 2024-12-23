package com.masterplus.animals.core.data.mapper

import ClassDto
import FamilyDto
import HabitatCategoryDto
import ImageDto
import MetadataDto
import OrderDto
import PhylumDto
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.ImageEntity
import com.masterplus.animals.core.shared_features.database.entity.ImageMetadataEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.ClassWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.FamilyWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.HabitatWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.ImageWithMetadataEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.OrderWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.PhylumWithImageEmbedded


fun ClassDto.toClassWithImageEmbedded(): ClassWithImageEmbedded{
    val imageModel = image?.toImageEntity()
    val metadata = image?.let { it.metadata?.toImageMetadataEntity(it.id) }
    return ClassWithImageEmbedded(
        classEntity = toClassEntity(),
        image = imageModel?.let { ImageWithMetadataEmbedded(it, metadata) }
    )
}

fun ClassDto.toClassEntity(): ClassEntity{
    return ClassEntity(
        id = id,
        class_en = class_en,
        class_tr = class_tr,
        phylum_id = phylum_id,
        image_id = image?.id,
        kingdom_id = kingdom_id,
        scientific_name = scientific_name,
        created_at = "",
        updated_at = ""
    )
}


fun FamilyDto.toFamilyEntity(): FamilyEntity {
    return FamilyEntity(
        id = id,
        family_en = family_en,
        family_tr = family_tr,
        kingdom_id = kingdom_id,
        order_id = order_id,
        scientific_name = scientific_name,
        image_id = image?.id,
        created_at = "",
        updated_at = ""
    )
}

fun FamilyDto.toFamilyWithImageEmbedded(): FamilyWithImageEmbedded {
    val imageModel = image?.toImageEntity()
    val metadata = image?.let { it.metadata?.toImageMetadataEntity(it.id) }
    return FamilyWithImageEmbedded(
        family = toFamilyEntity(),
        image = imageModel?.let { ImageWithMetadataEmbedded(it, metadata) }
    )
}




fun HabitatCategoryDto.toHabitatCategoryEntity(): HabitatCategoryEntity {
    return HabitatCategoryEntity(
        id = id,
        habitat_category_en = habitat_category_en,
        habitat_category_tr = habitat_category_tr,
        image_id = image?.id,
    )
}

fun HabitatCategoryDto.toHabitatCategoryWithImageEmbedded(): HabitatWithImageEmbedded {
    val imageModel = image?.toImageEntity()
    val metadata = image?.let { it.metadata?.toImageMetadataEntity(it.id) }
    return HabitatWithImageEmbedded(
        habitat = toHabitatCategoryEntity(),
        image = imageModel?.let { ImageWithMetadataEmbedded(it, metadata) }
    )
}



fun PhylumDto.toPhylumEntity(): PhylumEntity {
    return PhylumEntity(
        id = id,
        kingdom_id = kingdom_id,
        phylum_en = phylum_en,
        phylum_tr = phylum_tr,
        scientific_name = scientific_name,
        image_id = image?.id,
    )
}

fun PhylumDto.toPhylumWithImageEmbedded(): PhylumWithImageEmbedded {
    val imageModel = image?.toImageEntity()
    val metadata = image?.let { it.metadata?.toImageMetadataEntity(it.id) }
    return PhylumWithImageEmbedded(
        phylum = toPhylumEntity(),
        image = imageModel?.let { ImageWithMetadataEmbedded(it, metadata) }
    )
}


fun OrderDto.toOrderEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        class_id = class_id,
        kingdom_id = kingdom_id,
        order_en = order_en,
        order_tr = order_tr,
        scientific_name = scientific_name,
        image_id = image?.id,
        created_at = "",
        updated_at = ""
    )
}

fun OrderDto.toOrderWithImageEmbedded(): OrderWithImageEmbedded {
    val imageModel = image?.toImageEntity()
    val metadata = image?.let { it.metadata?.toImageMetadataEntity(it.id) }
    return OrderWithImageEmbedded(
        order = toOrderEntity(),
        image = imageModel?.let { ImageWithMetadataEmbedded(it, metadata) }
    )
}




fun MetadataDto.toImageMetadataEntity(imageId: Int): ImageMetadataEntity {
    return ImageMetadataEntity(
        id = null,
        image_id = imageId,
        artist_name = artist_name,
        license_url = license_url,
        usage_terms = usage_terms,
        image_description = image_description,
        date_time_original = date_time_original,
        license_short_name = license_short_name,
        description_url = description_url
    )
}


fun ImageDto.toImageEntity(): ImageEntity{
    return ImageEntity(
        id = id,
        image_url = image_url,
        image_path = image_path,
        created_at = "",
        updated_at = ""
    )
}


