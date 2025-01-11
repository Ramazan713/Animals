package com.masterplus.animals.features.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.domain.repo.ConnectivityObserver
import com.masterplus.animals.core.domain.services.CheckDayChangeService
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.remote_config.domain.repo.RemoteConfigRepo
import com.masterplus.animals.core.shared_features.select_font_size.domain.repo.SelectFontSizeRepo
import com.masterplus.animals.features.kingdom.domain.repo.DailySpeciesRepo
import com.masterplus.animals.features.search.domain.repo.SearchAdRepo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val remoteConfigRepo: RemoteConfigRepo,
    private val serverReadCounter: ServerReadCounter,
    private val searchAdRepo: SearchAdRepo,
    private val dailySpeciesRepo: DailySpeciesRepo,
    private val checkDayChangeService: CheckDayChangeService,
    networkObserver: ConnectivityObserver,
    fontSizeRepo: SelectFontSizeRepo
): ViewModel() {

    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            remoteConfigRepo.init()
        }

        networkObserver
            .isConnected
            .onEach { isConnected->
                _state.update { it.copy(isNetworkConnected = isConnected) }
            }
            .launchIn(viewModelScope)

        remoteConfigRepo
            .handleUpdates(viewModelScope)

        fontSizeRepo
            .fontSizeFlow
            .onEach { fontSizeEnum ->
                _state.update { it.copy(fontSizeEnum = fontSizeEnum) }
            }
            .launchIn(viewModelScope)

        checkDaily()
    }


    private fun checkDaily(){
        viewModelScope.launch {
            val isNewDay = checkDayChangeService.isNewDay()
            val jobs = listOf(
                async { serverReadCounter.checkNewDayAndReset(isNewDay) },
                async { searchAdRepo.checkNewDay(isNewDay) },
                async { dailySpeciesRepo.checkDaily(isNewDay) }
            )
            jobs.awaitAll()
        }
    }

}