package com.masterplus.animals.core.shared_features.savepoint.domain.di

import com.masterplus.animals.core.shared_features.savepoint.domain.use_cases.SavePointCategoryImageInfoUseCase
import com.masterplus.animals.core.shared_features.savepoint.domain.use_cases.SavePointSuggestedTitleUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val savePointDomainModule = module {
    singleOf(::SavePointCategoryImageInfoUseCase)
    singleOf(::SavePointSuggestedTitleUseCase)
}