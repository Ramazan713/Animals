package com.masterplus.animals.core.shared_features.translation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import kotlinx.coroutines.launch

class TranslationViewModel(
    private val translationRepo: TranslationRepo
): ViewModel() {


    fun setLanguageByCode(code: String?){
        viewModelScope.launch {
            val language = code?.let { LanguageEnum.fromCode(it) } ?: LanguageEnum.defaultValue
            translationRepo.setLanguage(language)
        }
    }
}