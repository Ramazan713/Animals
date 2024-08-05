package com.masterplus.animals.core.shared_features.savepoint.domain.enums

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePointDestinationTypeId


sealed class SavePointDestination(
    val destinationId: Int?,
    val saveKey: String? = null,
    val destinationType: SavePointDestinationType
){
    val title = destinationType.title
    val destinationTypeId = destinationType.destinationTypeId

    data object All: SavePointDestination(
        destinationId = null,
        destinationType = SavePointDestinationType.All
    ) {
        fun from(destinationId: Int?, saveKey: String?): All {
            return All
        }
    }

    data class ListType(val listId: Int): SavePointDestination(
        destinationId = listId,
        destinationType = SavePointDestinationType.ListType
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.ListType.destinationTypeId
            fun from(destinationId: Int?, saveKey: String?): ListType {
                return ListType(destinationId ?: 0)
            }
        }
    }

    data class Habitat(val habitatId: Int): SavePointDestination(
        destinationId = habitatId,
        destinationType = SavePointDestinationType.Habitat
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.Habitat.destinationTypeId
            fun from(destinationId: Int?, saveKey: String?): Habitat {
                return Habitat(destinationId ?: 0)
            }
        }
    }

    data class ClassType(val classId: Int): SavePointDestination(
        destinationId = classId,
        destinationType = SavePointDestinationType.ClassType
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.ClassType.destinationTypeId
            fun from(destinationId: Int?, saveKey: String?): ClassType {
                return ClassType(destinationId ?: 0)
            }
        }
    }

    data class Order(val orderId: Int): SavePointDestination(
        destinationId = orderId,
        destinationType = SavePointDestinationType.Order
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.Order.destinationTypeId
            fun from(destinationId: Int?, saveKey: String?): Order {
                return Order(destinationId ?: 0)
            }
        }
    }

    data class Family(val familyId: Int): SavePointDestination(
        destinationId = familyId,
        destinationType = SavePointDestinationType.Family
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.Family.destinationTypeId
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
                SavePointDestinationType.All.destinationTypeId -> All.from(destinationId, saveKey)
                SavePointDestinationType.ListType.destinationTypeId -> ListType.from(destinationId, saveKey)
                SavePointDestinationType.Habitat.destinationTypeId -> Habitat.from(destinationId, saveKey)
                SavePointDestinationType.ClassType.destinationTypeId -> ClassType.from(destinationId, saveKey)
                SavePointDestinationType.Order.destinationTypeId -> Order.from(destinationId, saveKey)
                SavePointDestinationType.Family.destinationTypeId -> Family.from(destinationId, saveKey)
                else -> null
            }
        }

        fun fromCategoryType(
            categoryType: CategoryType,
            destinationId: Int?,
        ): SavePointDestination {
            val destinationTypeId = when {
                destinationId != null -> All.destinationTypeId
                else -> categoryType.toSavePointDestinationTypeId(1)
            }
            return from(
                destinationTypeId = destinationTypeId,
                destinationId = destinationId
            ) ?: All
        }
    }

}
