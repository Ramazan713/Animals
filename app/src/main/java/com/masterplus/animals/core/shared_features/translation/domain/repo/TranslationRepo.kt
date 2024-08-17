package com.masterplus.animals.core.shared_features.translation.domain.repo

import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.coroutines.flow.Flow

interface TranslationRepo {

    fun getFlowLanguage(): Flow<LanguageEnum>

    suspend fun getLanguage(): LanguageEnum

    suspend fun setLanguage(languageEnum: LanguageEnum)
}