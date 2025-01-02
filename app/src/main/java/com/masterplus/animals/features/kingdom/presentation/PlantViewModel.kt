package com.masterplus.animals.features.kingdom.presentation

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo

class PlantViewModel(
    categoryRepo: CategoryRepo,
    savePointRepo: SavePointRepo,
    translationRepo: TranslationRepo,
    appConfigPreferences: AppConfigPreferences
): KingdomBaseViewModel(
    categoryRepo = categoryRepo,
    savePointRepo = savePointRepo,
    translationRepo = translationRepo,
    appConfigPreferences = appConfigPreferences
) {
    override val kingdomType: KingdomType
        get() = KingdomType.Plants
}