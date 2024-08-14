package com.masterplus.animals.core.domain.enums

enum class ContentType(
    val contentTypeId: Int
) {
    Category(
        contentTypeId = 1
    ),
    Content(
        contentTypeId = 2
    );

    companion object {
        fun from(contentTypeId: Int): ContentType? {
            for(type in ContentType.entries){
                if (type.contentTypeId == contentTypeId) return type
            }
            return null
        }
    }
}