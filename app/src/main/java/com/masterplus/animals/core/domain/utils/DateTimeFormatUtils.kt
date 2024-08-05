package com.masterplus.animals.core.domain.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.MonthNames

object DateTimeFormatUtils {

    val dateTimeUntilMinuteFormat = LocalDateTime.Format {
        dayOfMonth()
        chars(" ")
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        chars(" ")
        year()
        chars(" ")

        hour()
        chars(":")
        minute()
    }
}