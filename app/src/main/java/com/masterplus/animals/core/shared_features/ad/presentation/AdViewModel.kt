package com.masterplus.animals.core.shared_features.ad.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.shared_features.ad.domain.repo.InterstitialAdRepo
import com.masterplus.animals.core.shared_features.ad.domain.repo.ReadCounterRewardAdRepo
import com.masterplus.animals.features.category_list.presentation.navigation.CategoryListRoute
import com.masterplus.animals.features.category_list.presentation.navigation.CategoryListWithDetailRoute
import com.masterplus.animals.features.search.presentation.navigation.SearchCategoryRoute
import com.masterplus.animals.features.search.presentation.navigation.SearchSpeciesRoute
import com.masterplus.animals.features.species_detail.presentation.navigation.SpeciesDetailRoute
import com.masterplus.animals.features.species_list.presentation.navigation.SpeciesListRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdViewModel(
    private val interstitialAdRepo: InterstitialAdRepo,
    private val readCounterRewardAdRepo: ReadCounterRewardAdRepo
): ViewModel(){

    private val _state = MutableStateFlow(AdState())
    val state get() = _state.asStateFlow()

    init {
        interstitialAdRepo
            .showAdFlow
            .distinctUntilChanged()
            .filter { it }
            .onEach {
                _state.update { it.copy(
                    uiEvent = AdUiEvent.LoadInterstitialAd
                ) }
                interstitialAdRepo.stopConsumeSeconds()
            }
            .launchIn(viewModelScope)

        readCounterRewardAdRepo
            .contentShowAdFlow
            .distinctUntilChanged()
            .filter { it }
            .onEach {
                _state.update { it.copy(
                    uiEvent = AdUiEvent.LoadRewordedAd(ContentType.Content)
                ) }
            }
            .launchIn(viewModelScope)

        readCounterRewardAdRepo
            .categoryShowAdFlow
            .distinctUntilChanged()
            .filter { it }
            .onEach {
                _state.update { it.copy(
                    uiEvent = AdUiEvent.LoadRewordedAd(ContentType.Category)
                ) }
            }
            .launchIn(viewModelScope)

    }

    fun onAction(action: AdAction){
        when(action){
            is AdAction.OnDestinationChange -> {
                if(_state.value.isPremiumActive) return
                if(containsAdRoute(action.currentDestination)){
                    interstitialAdRepo.startConsumeSeconds()
                    interstitialAdRepo.increaseOpeningCounter()
                    if(_state.value.uiEvent == null){
                        _state.update { it.copy(
                            uiEvent = AdUiEvent.CheckInterstitialAdShowStatus
                        ) }
                    }
                }else{
                    interstitialAdRepo.stopConsumeSeconds()
                }
            }
            AdAction.ClearUiEvent -> {
                _state.update { it.copy(uiEvent = null) }
            }
            AdAction.ClearUiResult -> {
                _state.update { it.copy(
                    uiResult = null
                ) }
            }
            AdAction.ResetCounter -> {
                interstitialAdRepo.resetValues()
            }

            is AdAction.SetPremiumActive -> {
                _state.update { it.copy(
                    isPremiumActive = action.premiumActive
                ) }
                if(action.premiumActive){
                    interstitialAdRepo.stopConsumeSeconds()
                }
            }

            is AdAction.RequestShowRewardAd -> {
                _state.update { it.copy(
                    uiEvent = AdUiEvent.ShowRewordedAd(action.contentType)
                ) }
            }

            is AdAction.ResetReadCounter -> {
                viewModelScope.launch {
                    _state.update { it.copy(
                        uiResult = AdUiResult.OnShowingRewardSuccess
                    ) }
                    readCounterRewardAdRepo.resetCounter(action.contentType)
                }
            }
        }
    }

    private fun containsAdRoute(
        currentDestination: NavDestination?
    ): Boolean{
        return AD_COUNTER_ROUTES.find { route ->
            currentDestination?.hierarchy?.any { it.hasRoute(route) } != false
        } != null
    }

    companion object {
        val AD_COUNTER_ROUTES = listOf(
            CategoryListRoute::class,
            CategoryListWithDetailRoute::class,
            SearchCategoryRoute::class,
            SearchSpeciesRoute::class,
            SpeciesDetailRoute::class,
            SpeciesListRoute::class
        )
    }

}
