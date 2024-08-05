package com.masterplus.animals.core.shared_features.savepoint.domain.enums

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.utils.UiText


sealed class SavePointDestination(
    val destinationTypeId: Int,
    val destinationId: Int?,
    val saveKey: String? = null,
    val title: UiText
){
    data object All: SavePointDestination(
        destinationTypeId = 1,
        destinationId = null,
        title = UiText.Text("Tümü")
    ) {
        fun from(destinationId: Int?, saveKey: String?): All {
            return All
        }
    }

    data class ListType(val listId: Int): SavePointDestination(
        destinationTypeId = DESTINATION_TYPE_ID,
        destinationId = listId,
        title = UiText.Text("Liste")
    ) {
        companion object {
            const val DESTINATION_TYPE_ID = 2
            fun from(destinationId: Int?, saveKey: String?): ListType {
                return ListType(destinationId ?: 0)
            }
        }
    }

    data class Habitat(val habitatId: Int): SavePointDestination(
        destinationTypeId = DESTINATION_TYPE_ID,
        destinationId = habitatId,
        title = UiText.Text("Habitat")
    ) {
        companion object {
            const val DESTINATION_TYPE_ID = 3
            fun from(destinationId: Int?, saveKey: String?): Habitat {
                return Habitat(destinationId ?: 0)
            }
        }
    }

    data class ClassType(val classId: Int): SavePointDestination(
        destinationTypeId = DESTINATION_TYPE_ID,
        destinationId = classId,
        title = UiText.Text("Sınıf")
    ) {
        companion object {
            const val DESTINATION_TYPE_ID = 4
            fun from(destinationId: Int?, saveKey: String?): ClassType {
                return ClassType(destinationId ?: 0)
            }
        }
    }

    data class Order(val orderId: Int): SavePointDestination(
        destinationTypeId = DESTINATION_TYPE_ID,
        destinationId = orderId,
        title = UiText.Text("Takım")
    ) {
        companion object {
            const val DESTINATION_TYPE_ID = 5
            fun from(destinationId: Int?, saveKey: String?): Order {
                return Order(destinationId ?: 0)
            }
        }
    }

    data class Family(val familyId: Int): SavePointDestination(
        destinationTypeId = DESTINATION_TYPE_ID,
        destinationId = familyId,
        title = UiText.Text("Familya")
    ) {
        companion object {
            const val DESTINATION_TYPE_ID = 6
            fun from(destinationId: Int?, saveKey: String?): SavePointDestination {
                return Family(destinationId ?: 0)
            }
        }
    }

    companion object {
        val All_DESTINATION_TYPE_IDS = listOf(
            All.destinationTypeId,
            ListType.DESTINATION_TYPE_ID,
            Habitat.DESTINATION_TYPE_ID,
            ClassType.DESTINATION_TYPE_ID,
            Order.DESTINATION_TYPE_ID,
            Family.DESTINATION_TYPE_ID
        )
        fun from(
            destinationTypeId: Int,
            destinationId: Int?,
            saveKey: String? = null
        ): SavePointDestination?{
            return when(destinationTypeId){
                All.destinationTypeId -> All.from(destinationId, saveKey)
                ListType.DESTINATION_TYPE_ID -> ListType.from(destinationId, saveKey)
                Habitat.DESTINATION_TYPE_ID -> Habitat.from(destinationId, saveKey)
                ClassType.DESTINATION_TYPE_ID -> ClassType.from(destinationId, saveKey)
                Order.DESTINATION_TYPE_ID -> Order.from(destinationId, saveKey)
                Family.DESTINATION_TYPE_ID -> Family.from(destinationId, saveKey)
                else -> null
            }
        }

        fun fromCategoryType(
            categoryType: CategoryType,
            destinationId: Int?,
        ): SavePointDestination {
            val destinationTypeId = when {
                destinationId != null -> All.destinationTypeId
                else -> when(categoryType){
                    CategoryType.Habitat -> Habitat.DESTINATION_TYPE_ID
                    CategoryType.Class -> ClassType.DESTINATION_TYPE_ID
                    CategoryType.Order -> Order.DESTINATION_TYPE_ID
                    CategoryType.Family -> Family.DESTINATION_TYPE_ID
                    CategoryType.List -> ListType.DESTINATION_TYPE_ID
                    else -> All.destinationTypeId
                }
            }
            return from(
                destinationTypeId = destinationTypeId,
                destinationId = destinationId
            ) ?: All
        }
    }

}
