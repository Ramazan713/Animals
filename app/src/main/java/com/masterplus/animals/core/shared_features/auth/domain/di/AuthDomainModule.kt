package com.masterplus.animals.core.shared_features.auth.domain.di

import com.masterplus.animals.core.shared_features.auth.domain.use_cases.ValidateEmailUseCase
import com.masterplus.animals.core.shared_features.auth.domain.use_cases.ValidatePasswordUseCase
import org.koin.dsl.module

val authDomainModule = module {
    single { ValidateEmailUseCase() }
    single { ValidatePasswordUseCase() }
}