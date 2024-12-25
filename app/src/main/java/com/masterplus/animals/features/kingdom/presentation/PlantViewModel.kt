package com.masterplus.animals.features.kingdom.presentation

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo

class PlantViewModel(
    categoryRepo: CategoryRepo,
    savePointRepo: SavePointRepo,
    translationRepo: TranslationRepo
): KingdomBaseViewModel(
    categoryRepo = categoryRepo,
    savePointRepo = savePointRepo,
    translationRepo = translationRepo
) {
    override val kingdomType: KingdomType
        get() = KingdomType.Plants
}