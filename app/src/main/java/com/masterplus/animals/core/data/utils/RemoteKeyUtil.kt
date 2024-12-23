package com.masterplus.animals.core.data.utils

import com.masterplus.animals.core.domain.enums.KingdomType

object RemoteKeyUtil {

    fun getPhylumRemoteKey(
        kingdomType: KingdomType,
    ): String{
        return "PhylumKey-${kingdomType.kingdomId}"
    }

    fun getClassRemoteKey(
        kingdomType: KingdomType,
        phylumId: Int?
    ): String{
        return "ClassKey-${kingdomType.kingdomId}-${phylumId ?: 0}"
    }

    fun getOrderRemoteKey(
        kingdomType: KingdomType,
        classId: Int?
    ): String{
        return "OrderKey-${kingdomType.kingdomId}-${classId ?: 0}"
    }

    fun getFamilyRemoteKey(
        kingdomType: KingdomType,
        orderId: Int?
    ): String{
        return "FamilyKey-${kingdomType.kingdomId}-${orderId ?: 0}"
    }

    fun getHabitatRemoteKey(
        kingdomType: KingdomType,
    ): String{
        return "HabitatKey-${kingdomType.kingdomId}"
    }
}