package com.masterplus.animals.core.domain.constants

import com.masterplus.animals.core.shared_features.preferences.domain.model.PrefKey

object KPref {

    val backupMetaCounter = PrefKey("backupMetaCounter",0L)
    val inAppReviewDay = PrefKey("inAppReviewDay",0L)
    val thresholdOpeningCount = PrefKey("thresholdOpeningCount",15)
    val consumeIntervalSeconds = PrefKey("consumeIntervalSeconds",4)
    val thresholdConsumeSeconds = PrefKey("thresholdConsumeSeconds",80)
}