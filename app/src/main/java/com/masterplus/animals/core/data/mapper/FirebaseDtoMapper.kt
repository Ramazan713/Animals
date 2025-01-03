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


fun ClassDto.toClassWithImageEmbedded(label: String): ClassWithImageEmbedded{
    val imageModel = image?.toImageEntity()
    val metadata = image?.let { it.metadata?.toImageMetadataEntity(it.id) }
    return ClassWithImageEmbedded(
        classEntity = toClassEntity(label),
        image = imageModel?.let { ImageWithMetadataEmbedded(it, metadata) }
    )
}

fun ClassDto.toClassEntity(label: String): ClassEntity{
    return ClassEntity(
        id = id,
        class_en = class_en,
        class_tr = class_tr,
        phylum_id = phylum_id,
        image_id = image?.id,
        kingdom_id = kingdom_id,
        scientific_name = scientific_name,
        label = label,
        order_key = order_key
    )
}


fun FamilyDto.toFamilyEntity(label: String): FamilyEntity {
    return FamilyEntity(
        id = id,
        family_en = family_en,
        family_tr = family_tr,
        kingdom_id = kingdom_id,
        order_id = order_id,
        scientific_name = scientific_name,
        image_id = image?.id,
        label = label,
        order_key = order_key
    )
}

fun FamilyDto.toFamilyWithImageEmbedded(label: String): FamilyWithImageEmbedded {
    return FamilyWithImageEmbedded(
        family = toFamilyEntity(label),
        image = image?.toImageWithMetadata()
    )
}




fun HabitatCategoryDto.toHabitatCategoryEntity(label: String): HabitatCategoryEntity {
    return HabitatCategoryEntity(
        id = id,
        habitat_category_en = habitat_category_en,
        habitat_category_tr = habitat_category_tr,
        image_id = image?.id,
        label = label
    )
}

fun HabitatCategoryDto.toHabitatCategoryWithImageEmbedded(label: String): HabitatWithImageEmbedded {
    return HabitatWithImageEmbedded(
        habitat = toHabitatCategoryEntity(label),
        image = image?.toImageWithMetadata()
    )
}



fun PhylumDto.toPhylumEntity(label: String): PhylumEntity {
    return PhylumEntity(
        id = id,
        kingdom_id = kingdom_id,
        phylum_en = phylum_en,
        phylum_tr = phylum_tr,
        scientific_name = scientific_name,
        image_id = image?.id,
        label = label,
        order_key = order_key
    )
}

fun PhylumDto.toPhylumWithImageEmbedded(label: String): PhylumWithImageEmbedded {
    return PhylumWithImageEmbedded(
        phylum = toPhylumEntity(label),
        image = image?.toImageWithMetadata()
    )
}


fun OrderDto.toOrderEntity(label: String): OrderEntity {
    return OrderEntity(
        id = id,
        class_id = class_id,
        kingdom_id = kingdom_id,
        order_en = order_en,
        order_tr = order_tr,
        scientific_name = scientific_name,
        image_id = image?.id,
        label = label,
        order_key = order_key
    )
}

fun OrderDto.toOrderWithImageEmbedded(label: String): OrderWithImageEmbedded {
    return OrderWithImageEmbedded(
        order = toOrderEntity(label),
        image = image?.toImageWithMetadata()
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
        image_path = image_path
    )
}

fun ImageDto.toImageWithMetadata(): ImageWithMetadataEmbedded{
    return ImageWithMetadataEmbedded(
        image = toImageEntity(),
        metadata = metadata?.toImageMetadataEntity(id)
    )
}
