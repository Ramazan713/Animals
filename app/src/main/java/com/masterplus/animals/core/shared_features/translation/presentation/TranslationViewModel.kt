package com.masterplus.animals.core.shared_features.translation.presentation

import android.os.Build
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.domain.constants.KPref
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import kotlinx.coroutines.launch

class TranslationViewModel(
    private val translationRepo: TranslationRepo,
    private val appPreferences: AppPreferences
): ViewModel() {

    fun setLanguageByCode(code: String?){
        viewModelScope.launch {
            val language = code?.let { LanguageEnum.fromCode(it) } ?: LanguageEnum.defaultValue
            translationRepo.setLanguage(language)
        }
    }

    fun checkLanguageFirstTime(){
        viewModelScope.launch {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) return@launch
            val locale = LocaleListCompat.getDefault()[0] ?: return@launch
            if(appPreferences.getItem(KPref.checkInitLang)) return@launch

            val language = LanguageEnum.fromCode(locale.language)
            translationRepo.setLanguage(language)
            appPreferences.setItem(KPref.checkInitLang, true)
        }
    }
}