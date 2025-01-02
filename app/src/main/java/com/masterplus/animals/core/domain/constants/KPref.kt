package com.masterplus.animals.core.domain.constants

import com.masterplus.animals.core.shared_features.preferences.domain.model.PrefKey

object KPref {

    val backupMetaCounter = PrefKey("backupMetaCounter",0L)
    val inAppReviewDay = PrefKey("inAppReviewDay",0L)

    val contentReadCounter = PrefKey("contentReadCounter",0)
    val categoryReadCounter = PrefKey("categoryReadCounter",0)
    val lastUpdatedReadCounter = PrefKey("lastUpdatedReadCounter","")
}