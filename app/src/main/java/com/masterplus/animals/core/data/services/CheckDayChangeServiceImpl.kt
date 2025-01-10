package com.masterplus.animals.core.data.services

import com.google.firebase.functions.HttpsCallableOptions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.masterplus.animals.core.domain.constants.KPref
import com.masterplus.animals.core.domain.services.CheckDayChangeService
import com.masterplus.animals.core.shared_features.preferences.data.get
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class CheckDayChangeServiceImpl(
    private val appPreferences: AppPreferences
): CheckDayChangeService {

    override suspend fun isNewDay(): Boolean {
        val savedDate = appPreferences.getData()[KPref.newDayCheck]
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
        if(savedDate.isBlank()){
            appPreferences.setItem(KPref.newDayCheck, currentDate.toString())
            return true
        }
        if(currentDate == savedDate) return false

        val serverDate = getServerDate()?.toString() ?: return false
        if(serverDate == savedDate) return false

        appPreferences.setItem(KPref.newDayCheck, serverDate)
        return true
    }

    private suspend fun getServerDate(): LocalDate?{
        try{
            val result = Firebase.functions
                .getHttpsCallable("getCurrentDate")
                .call()
                .await()
            val data = result.getData() as Map<*, *>
            val date = Instant.parse(data["date"] as String)
            return date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }catch (e: Exception){
            return null
        }
    }
}