package com.masterplus.animals.core.shared_features.theme.data.repo

import android.os.Build
import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import com.masterplus.animals.core.shared_features.theme.domain.models.ThemeModel
import com.masterplus.animals.core.shared_features.theme.domain.repo.ThemeRepo
import javax.inject.Inject

class ThemeRepoImpl @Inject constructor(
    private val settingsPreferences: SettingsPreferences
): ThemeRepo {
    override suspend fun getThemeModel(): ThemeModel {
        val prefData = settingsPreferences.getData()
        return ThemeModel(
            themeEnum = prefData.themeEnum,
            useDynamicColor = prefData.useThemeDynamic,
            enabledDynamicColor = hasSupportedDynamicTheme()
        )
    }

    override suspend fun updateThemeModel(themeModel: ThemeModel) {
        settingsPreferences.updateData { pref->
            pref.copy(
                themeEnum = themeModel.themeEnum,
                useThemeDynamic = themeModel.useDynamicColor
            )
        }
    }

    override fun hasSupportedDynamicTheme(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
}