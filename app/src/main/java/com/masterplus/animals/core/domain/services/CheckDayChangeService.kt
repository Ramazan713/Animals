package com.masterplus.animals.core.domain.services

interface CheckDayChangeService {

    suspend fun isNewDay(): Boolean
}