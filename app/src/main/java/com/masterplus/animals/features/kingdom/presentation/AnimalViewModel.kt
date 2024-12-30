package com.masterplus.animals.features.kingdom.presentation
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo

class AnimalViewModel(
    categoryRepo: CategoryRepo,
    savePointRepo: SavePointRepo,
    translationRepo: TranslationRepo,
    appPreferences: AppPreferences
): KingdomBaseViewModel(
    categoryRepo = categoryRepo,
    savePointRepo = savePointRepo,
    translationRepo = translationRepo,
    appPreferences = appPreferences
) {
    override val kingdomType: KingdomType
        get() = KingdomType.Animals
}