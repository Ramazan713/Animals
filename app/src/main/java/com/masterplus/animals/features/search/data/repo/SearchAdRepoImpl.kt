package com.masterplus.animals.features.search.data.repo

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.masterplus.animals.core.domain.services.CheckDayChangeService
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import com.masterplus.animals.features.search.domain.repo.SearchAdRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class SearchAdRepoImpl(
    private val appConfigPreferences: AppConfigPreferences,
    private val appPreferences: AppPreferences,
    private val checkDayChangeService: CheckDayChangeService
): SearchAdRepo {

    override val remainingCategoryAdCount: Flow<Int>
        get() = appPreferences.dataFlow
            .map { getCategoryRemainingCounter(it) }
            .distinctUntilChanged()

    override val remainingAppAdCount: Flow<Int>
        get() = appPreferences.dataFlow
            .map { getAppRemainingCounter(it) }
            .distinctUntilChanged()

    override suspend fun resetCategoryAd() {
        val counter = appConfigPreferences.getData().ad.rewardCategorySearchCount
        appPreferences.edit {
            it[remainingCategoryAdCountKey] = getCategoryRemainingCounter(it) + counter
        }
    }

    override suspend fun decreaseCategoryAd() {
        appPreferences.edit {
            it[remainingCategoryAdCountKey] = getCategoryRemainingCounter(it) - 1
        }
    }

    override suspend fun resetAppAd() {
        val counter = appConfigPreferences.getData().ad.rewardAppSearchCount
        appPreferences.edit {
            it[remainingAppAdCountKey] = getAppRemainingCounter(it) + counter
        }
    }

    override suspend fun decreaseAppAd() {
        appPreferences.edit {
            it[remainingAppAdCountKey] = getAppRemainingCounter(it) - 1
        }
    }

    override suspend fun checkNewDay() {
        if(!checkDayChangeService.isNewDay()) return
        val appConfigData = appConfigPreferences.getData().ad
        appPreferences.edit {
            if(getCategoryRemainingCounter(it) <= 0){
                it[remainingCategoryAdCountKey] = appConfigData.initRemainingCategorySearchCount
            }
            if(getAppRemainingCounter(it) <= 0){
                it[remainingAppAdCountKey] = appConfigData.initRemainingAppSearchCount
            }
        }
    }


    private suspend fun getCategoryRemainingCounter(preferences: Preferences): Int{
        return preferences[remainingCategoryAdCountKey]
            ?: appConfigPreferences.getData().ad.initRemainingCategorySearchCount
    }

    private suspend fun getAppRemainingCounter(preferences: Preferences): Int{
        return preferences[remainingAppAdCountKey]
            ?: appConfigPreferences.getData().ad.initRemainingAppSearchCount
    }


    companion object{
        private val remainingCategoryAdCountKey = intPreferencesKey("remainingAdCategorySearchCount")
        private val remainingAppAdCountKey = intPreferencesKey("remainingAdAppSearchCount")
    }
}