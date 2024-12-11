package com.masterplus.animals.core.shared_features.translation.data.repo

import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.jupiter.api.Assertions.*

class TranslationRepoFake : TranslationRepo {
    var fakeLanguage: LanguageEnum = LanguageEnum.defaultValue

    val fakeFlow = MutableStateFlow(fakeLanguage)

    override fun getFlowLanguage(): Flow<LanguageEnum> {
        return fakeFlow
    }

    override suspend fun getLanguage(): LanguageEnum {
        return fakeLanguage
    }

    override suspend fun setLanguage(languageEnum: LanguageEnum) {
        fakeLanguage = languageEnum
        fakeFlow.value = languageEnum
    }

    fun updateFakeLanguage(languageEnum: LanguageEnum) {
        fakeLanguage = languageEnum
        fakeFlow.value = languageEnum
    }
}