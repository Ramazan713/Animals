package com.masterplus.animals.core.shared_features.savepoint.domain.enums

import com.masterplus.animals.core.domain.enums.ContentType

enum class SavePointContentType(
    val contentTypeId: Int
) {
    Category(
        contentTypeId = 1
    ),
    Content(
        contentTypeId = 2
    );

    val isCategory get() = Category == this
    val isContent get() = Content == this

    fun toContentType(): ContentType{
        return when(this){
            Category -> ContentType.Category
            Content -> ContentType.Content
        }
    }

    companion object {
        fun from(contentTypeId: Int): SavePointContentType? {
            for(type in SavePointContentType.entries){
                if (type.contentTypeId == contentTypeId) return type
            }
            return null
        }
    }
}