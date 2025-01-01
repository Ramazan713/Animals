package com.masterplus.animals.core.shared_features.ad.domain.repo

import kotlinx.coroutines.flow.Flow

interface InterstitialAdRepo {

    val showAdFlow: Flow<Boolean>

    fun increaseOpeningCounter()

    fun startConsumeSeconds()

    fun stopConsumeSeconds()

    fun resetValues()
}