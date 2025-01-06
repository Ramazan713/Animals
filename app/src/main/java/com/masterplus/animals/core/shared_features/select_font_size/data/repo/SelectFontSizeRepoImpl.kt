package com.masterplus.animals.core.shared_features.select_font_size.data.repo

import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import com.masterplus.animals.core.shared_features.select_font_size.domain.enums.FontSizeEnum
import com.masterplus.animals.core.shared_features.select_font_size.domain.repo.SelectFontSizeRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class SelectFontSizeRepoImpl(
    private val settingsPreferences: SettingsPreferences
): SelectFontSizeRepo {
    override val fontSizeFlow: Flow<FontSizeEnum>
        get() = settingsPreferences
            .dataFlow
            .map { it.fontSizeEnum }
            .distinctUntilChanged()

    override suspend fun getFontSize(): FontSizeEnum {
       return settingsPreferences.getData().fontSizeEnum
    }

    override suspend fun changeFontSize(fontSizeEnum: FontSizeEnum) {
        settingsPreferences.updateData { it.copy(
            fontSizeEnum = fontSizeEnum
        ) }
    }
}