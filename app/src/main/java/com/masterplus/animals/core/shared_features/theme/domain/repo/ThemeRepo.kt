package com.masterplus.animals.core.shared_features.theme.domain.repo

import com.masterplus.animals.core.shared_features.theme.domain.models.ThemeModel

interface ThemeRepo {

    suspend fun getThemeModel(): ThemeModel

    suspend fun updateThemeModel(themeModel: ThemeModel)

    fun hasSupportedDynamicTheme(): Boolean
}