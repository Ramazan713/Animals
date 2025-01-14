package com.masterplus.animals.core.domain.constants

import com.masterplus.animals.core.shared_features.preferences.domain.model.PrefKey

object KPref {
    val checkInitLang = PrefKey("checkInitLang", false)

    val backupMetaCounter = PrefKey("backupMetaCounter",0L)
    val inAppReviewDay = PrefKey("inAppReviewDay",0L)

    val contentReadCounter = PrefKey("contentReadCounter",0)
    val categoryReadCounter = PrefKey("categoryReadCounter",0)

    val removePagingNextKeyEnd = PrefKey("removePagingNextKeyEnd","")
    val setPagingShouldRefresh = PrefKey("setPagingShouldRefresh","")

    val newDayCheck = PrefKey("newDayCheck","")
}