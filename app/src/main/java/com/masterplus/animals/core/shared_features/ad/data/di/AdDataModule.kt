package com.masterplus.animals.core.shared_features.ad.data.di

import com.masterplus.animals.core.shared_features.ad.data.repo.InterstitialAdRepoImpl
import com.masterplus.animals.core.shared_features.ad.data.repo.ReadCounterRewardAdRepoImpl
import com.masterplus.animals.core.shared_features.ad.domain.repo.InterstitialAdRepo
import com.masterplus.animals.core.shared_features.ad.domain.repo.ReadCounterRewardAdRepo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val adDataModule = module {
    singleOf(::InterstitialAdRepoImpl).bind<InterstitialAdRepo>()
    singleOf(::ReadCounterRewardAdRepoImpl).bind<ReadCounterRewardAdRepo>()
}