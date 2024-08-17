package com.masterplus.animals.core.shared_features.translation.data.repo

import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class TranslationRepoImpl(
    private val appPreferences: SettingsPreferences
): TranslationRepo {

    override fun getFlowLanguage(): Flow<LanguageEnum> {
        return appPreferences.dataFlow.map { it.languageEnum }.distinctUntilChanged()
    }

    override suspend fun getLanguage(): LanguageEnum {
        return appPreferences.getData().languageEnum
    }

    override suspend fun setLanguage(languageEnum: LanguageEnum) {
        appPreferences.updateData {
            it.copy(
                languageEnum = languageEnum
            )
        }
    }
}