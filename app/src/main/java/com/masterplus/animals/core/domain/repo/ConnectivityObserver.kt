package com.masterplus.animals.core.domain.repo

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    val isConnected: Flow<Boolean>

    fun hasConnection(): Boolean
}