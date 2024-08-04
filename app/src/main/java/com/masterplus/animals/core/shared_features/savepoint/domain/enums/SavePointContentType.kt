package com.masterplus.animals.core.shared_features.savepoint.domain.enums

enum class SavePointContentType(
    val contentTypeId: Int
) {
    Category(
        contentTypeId = 1
    ),
    Content(
        contentTypeId = 2
    );

    companion object {
        fun from(contentTypeId: Int): SavePointContentType? {
            for(type in SavePointContentType.entries){
                if (type.contentTypeId == contentTypeId) return type
            }
            return null
        }
    }
}