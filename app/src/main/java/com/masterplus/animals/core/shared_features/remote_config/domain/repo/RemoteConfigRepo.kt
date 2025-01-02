package com.masterplus.animals.core.shared_features.remote_config.domain.repo

import kotlinx.coroutines.CoroutineScope

interface RemoteConfigRepo {

    suspend fun init()

    fun handleUpdates(scope: CoroutineScope)
}