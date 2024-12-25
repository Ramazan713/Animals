package com.masterplus.animals.core.shared_features.analytics.data.di

import com.masterplus.animals.core.shared_features.analytics.data.repo.FirebaseServerReadCounter
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val analyticsDataModule = module {
    singleOf(::FirebaseServerReadCounter).bind<ServerReadCounter>()
}