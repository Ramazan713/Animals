package com.masterplus.animals.features.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.domain.repo.ConnectivityObserver
import com.masterplus.animals.core.shared_features.remote_config.domain.repo.RemoteConfigRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val networkObserver: ConnectivityObserver,
    private val remoteConfigRepo: RemoteConfigRepo
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
    }

}