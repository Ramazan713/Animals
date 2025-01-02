package com.masterplus.animals.core.shared_features.ad.data.repo

import com.masterplus.animals.core.domain.constants.KPref
import com.masterplus.animals.core.shared_features.ad.domain.repo.InterstitialAdRepo
import com.masterplus.animals.core.shared_features.preferences.data.get
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class InterstitialAdRepoImpl(
    appConfigPreferences: AppConfigPreferences
): InterstitialAdRepo {

    private val consumeSecondsFlow = MutableStateFlow(0)
    private val openingCounterFlow = MutableStateFlow(0)

    private val timerEnabledFlow = MutableStateFlow(false)
    private val consumeIntervalSecondsFlow = appConfigPreferences.dataFlow
        .map { it.consumeIntervalSeconds }
        .distinctUntilChanged()
    private val thresholdValuesFlow = appConfigPreferences
        .dataFlow
        .map {
            ThresholdValues(
                thresholdOpeningCount = it.thresholdOpeningCount,
                thresholdConsumeSeconds = it.thresholdConsumeSeconds
            )
        }
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class, ObsoleteCoroutinesApi::class)
    private val totalConsumedSecondsFlow = combine(
        timerEnabledFlow,
        consumeIntervalSecondsFlow
    ){timerEnabled, consumeIntervalSeconds ->
        if(!timerEnabled) return@combine 0
        consumeIntervalSeconds
    }.flatMapLatest { intervalSeconds->
        if (intervalSeconds > 0) {
            flow {
                while (true) {
                    val consumedSeconds = intervalSeconds * 1000
                    delay(consumedSeconds.toLong())
                    emit(consumedSeconds)
                }
            }
        } else {
            emptyFlow()
        }
    }.onEach { consumedSeconds ->
        consumeSecondsFlow.value += consumedSeconds / 1000
    }.flatMapLatest {
        consumeSecondsFlow
    }


    override val showAdFlow: Flow<Boolean>
        get() = combine(
            totalConsumedSecondsFlow,
            openingCounterFlow,
            thresholdValuesFlow
        ){consumedSeconds, openingCounter, thresholdValues ->
            checkThreshold(consumedSeconds, openingCounter, thresholdValues)
        }

    override fun increaseOpeningCounter() {
        openingCounterFlow.value += 1
    }

    override fun startConsumeSeconds() {
        timerEnabledFlow.value = true
    }

    override fun stopConsumeSeconds() {
        timerEnabledFlow.value = false
    }

    override fun resetValues() {
        openingCounterFlow.value = 0
        consumeSecondsFlow.value = 0
    }


    private fun checkThreshold(consumedSeconds: Int, openingCounter: Int, thresholdValues: ThresholdValues): Boolean{
        return openingCounter >= thresholdValues.thresholdOpeningCount ||
                consumedSeconds >= thresholdValues.thresholdConsumeSeconds
    }
}

private data class ThresholdValues(
    val thresholdOpeningCount: Int,
    val thresholdConsumeSeconds: Int
)