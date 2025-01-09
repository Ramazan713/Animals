package com.masterplus.animals.core.shared_features.savepoint.domain.enums

import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePointDestinationTypeId


sealed class SavePointDestination(
    val destinationId: Int?,
    val saveKey: String? = null,
    val destinationType: SavePointDestinationType,
    open val kingdomType: KingdomType
){
    val title get() = destinationType.title
    val destinationTypeId get() = destinationType.destinationTypeId

    data class All(
        override val kingdomType: KingdomType
    ): SavePointDestination(
        destinationId = null,
        destinationType = SavePointDestinationType.All,
        kingdomType = kingdomType
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.All.destinationTypeId
            fun from(destinationId: Int?, kingdomType: KingdomType, saveKey: String?): All {
                return All(kingdomType)
            }
        }
    }

    data class ListType(val listId: Int): SavePointDestination(
        destinationId = listId,
        destinationType = SavePointDestinationType.ListType,
        kingdomType = KingdomType.DEFAULT
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.ListType.destinationTypeId
            fun from(destinationId: Int?, saveKey: String?): ListType {
                return ListType(destinationId ?: 0)
            }
        }
    }

    data class Habitat(
        val habitatId: Int,
        override val kingdomType: KingdomType
    ): SavePointDestination(
        destinationId = habitatId,
        destinationType = SavePointDestinationType.Habitat,
        kingdomType = kingdomType
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.Habitat.destinationTypeId
            fun from(destinationId: Int?, kingdomType: KingdomType, saveKey: String?): Habitat {
                return Habitat(destinationId ?: 0, kingdomType)
            }
        }
    }

    data class ClassType(
        val classId: Int,
        override val kingdomType: KingdomType
    ): SavePointDestination(
        destinationId = classId,
        destinationType = SavePointDestinationType.ClassType,
        kingdomType = kingdomType
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.ClassType.destinationTypeId
            fun from(destinationId: Int?, kingdomType: KingdomType, saveKey: String?): ClassType {
                return ClassType(destinationId ?: 0, kingdomType)
            }
        }
    }

    data class Order(
        val orderId: Int,
        override val kingdomType: KingdomType
    ): SavePointDestination(
        destinationId = orderId,
        destinationType = SavePointDestinationType.Order,
        kingdomType = kingdomType
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.Order.destinationTypeId
            fun from(destinationId: Int?, kingdomType: KingdomType, saveKey: String?): Order {
                return Order(destinationId ?: 0, kingdomType)
            }
        }
    }

    data class Family(
        val familyId: Int,
        override val kingdomType: KingdomType
    ): SavePointDestination(
        destinationId = familyId,
        destinationType = SavePointDestinationType.Family,
        kingdomType = kingdomType
    ) {
        companion object {
            val DESTINATION_TYPE_ID = SavePointDestinationType.Family.destinationTypeId
            fun from(destinationId: Int?, kingdomType: KingdomType, saveKey: String?): SavePointDestination {
                return Family(destinationId ?: 0, kingdomType)
            }
        }
    }

    fun toCategoryType(): CategoryType? {
        return when(this){
            is All -> null
            is ClassType -> CategoryType.Class
            is Family -> CategoryType.Family
            is Habitat -> CategoryType.Habitat
            is ListType -> CategoryType.List
            is Order -> CategoryType.Order
        }
    }

    fun toLabel(contentType: SavePointContentType): String{
        if(this is All && contentType.isContent){
            return RemoteKeyUtil.getSpeciesKingdomRemoteKey(kingdomType)
        }
        destinationId
        val categoryType = toCategoryType() ?: return RemoteKeyUtil.DEFAULT
        if(contentType.isContent){
            return RemoteKeyUtil.getSpeciesCategoryRemoteKey(
                categoryType = categoryType,
                itemId = destinationId,
                kingdomType = kingdomType
            )
        }
        return RemoteKeyUtil.getRemoteKeyWithCategoryType(
            kingdomType = kingdomType,
            categoryType = categoryType,
            parentItemId = destinationId
        )
    }



    companion object {
        fun from(
            destinationTypeId: Int,
            destinationId: Int?,
            kingdomType: KingdomType,
            saveKey: String? = null
        ): SavePointDestination?{
            return when(destinationTypeId){
                SavePointDestinationType.All.destinationTypeId -> All.from(destinationId, kingdomType, saveKey)
                SavePointDestinationType.ListType.destinationTypeId -> ListType.from(destinationId, saveKey)
                SavePointDestinationType.Habitat.destinationTypeId -> Habitat.from(destinationId, kingdomType , saveKey)
                SavePointDestinationType.ClassType.destinationTypeId -> ClassType.from(destinationId, kingdomType, saveKey)
                SavePointDestinationType.Order.destinationTypeId -> Order.from(destinationId, kingdomType ,saveKey)
                SavePointDestinationType.Family.destinationTypeId -> Family.from(destinationId, kingdomType ,saveKey)
                else -> null
            }
        }

        fun fromCategoryType(
            categoryType: CategoryType,
            destinationId: Int?,
            kingdomType: KingdomType,
            returnAll: Boolean = true
        ): SavePointDestination {
            val destinationTypeId = categoryType.toSavePointDestinationTypeId(
                itemId = destinationId,
                returnAll = returnAll
            )
            return from(
                destinationTypeId = destinationTypeId,
                destinationId = destinationId,
                kingdomType = kingdomType
            ) ?: All(kingdomType)
        }
    }

}
