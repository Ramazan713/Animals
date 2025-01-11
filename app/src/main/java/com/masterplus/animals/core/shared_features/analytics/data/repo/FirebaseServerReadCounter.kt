package com.masterplus.animals.core.shared_features.analytics.data.repo

import com.masterplus.animals.core.domain.constants.KPref
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.preferences.data.get
import com.masterplus.animals.core.shared_features.preferences.data.set
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class FirebaseServerReadCounter(
    private val appPreferences: AppPreferences,
): ServerReadCounter {

    override val contentCountersFlow: Flow<Int>
        get() = appPreferences.dataFlow.map { it[KPref.contentReadCounter] }.distinctUntilChanged()
    override val categoryCountersFlow: Flow<Int>
        get() = appPreferences.dataFlow.map { it[KPref.categoryReadCounter] }.distinctUntilChanged()

    override suspend fun getCounter(contentType: ContentType): Int {
        return when(contentType){
            ContentType.Category -> appPreferences.getItem(KPref.categoryReadCounter)
            ContentType.Content -> appPreferences.getItem(KPref.contentReadCounter)
        }
    }

    override suspend fun addCounter(contentType: ContentType, number: Int) {
        appPreferences.edit {
            if(contentType.isContent){
                it[KPref.contentReadCounter] += number
            }
            else{
                it[KPref.categoryReadCounter] += number
            }

        }
    }

    override suspend fun resetCounter(contentType: ContentType) {
        appPreferences.edit {
            if(contentType.isContent){
                it[KPref.contentReadCounter] = 0
            }
            else{
                it[KPref.categoryReadCounter] = 0
            }
        }
    }

    override suspend fun checkNewDayAndReset(isNewDay: Boolean) {
        if(!isNewDay) return
        appPreferences.edit {
            it[KPref.contentReadCounter] = 0
            it[KPref.categoryReadCounter] = 0
        }
    }
}