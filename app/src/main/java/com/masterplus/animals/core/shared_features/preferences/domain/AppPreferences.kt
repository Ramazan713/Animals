package com.masterplus.animals.core.shared_features.preferences.domain

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import com.masterplus.animals.core.shared_features.preferences.domain.model.EnumPrefKey
import com.masterplus.animals.core.shared_features.preferences.domain.model.IEnumPrefValue
import com.masterplus.animals.core.shared_features.preferences.domain.model.PrefKey
import kotlinx.coroutines.flow.Flow

interface AppPreferences {

    val dataFlow: Flow<Preferences>

    suspend fun getData(): Preferences

    suspend fun edit(transform: suspend (MutablePreferences) -> Unit)

    suspend fun <T> getItem(key: Preferences.Key<T>, defaultValue: T): T

    suspend fun <T> setItem(key: Preferences.Key<T>, value: T)

    suspend fun <T> setItem(item: PrefKey<T>, value: T)

    suspend fun<T> getItem(item: PrefKey<T>): T

    suspend fun <E:Enum<E>> setEnumItem(criteria: EnumPrefKey<E>, value: IEnumPrefValue)

    suspend fun <E:Enum<E>> getEnumItem(criteria: EnumPrefKey<E>): E

    suspend fun clear()

    fun dispose()

}