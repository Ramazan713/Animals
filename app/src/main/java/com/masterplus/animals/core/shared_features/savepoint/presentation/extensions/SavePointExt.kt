package com.masterplus.animals.core.shared_features.savepoint.presentation.extensions

import com.masterplus.animals.core.domain.utils.DateTimeFormatUtils
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import kotlinx.datetime.format

fun SavePoint.asReadableModifiedData(): String{
    return modifiedTime.format(DateTimeFormatUtils.dateTimeUntilMinuteFormat)
}