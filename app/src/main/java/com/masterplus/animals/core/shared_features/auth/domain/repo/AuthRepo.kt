package com.masterplus.animals.core.shared_features.auth.domain.repo

import com.google.firebase.auth.AuthCredential
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.EmptyResult
import com.masterplus.animals.core.domain.utils.ErrorText
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType
import com.masterplus.animals.core.shared_features.auth.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepo {

    fun userFlow(): Flow<User?>

    fun isLogin(): Boolean

    fun currentUser(): User?

    suspend fun signInWithEmail(email: String, password: String): DefaultResult<User>

    suspend fun signUpWithEmail(email: String, password: String): DefaultResult<User>

    suspend fun signInWithCredential(credential: AuthCredential): DefaultResult<User>

    suspend fun linkWithCredential(credential: AuthCredential): EmptyResult<ErrorText>

    suspend fun unLinkWith(providerType: AuthProviderType): EmptyResult<ErrorText>

    suspend fun resetPassword(email: String): DefaultResult<UiText>

    suspend fun signOut(): DefaultResult<UiText>

    suspend fun deleteUser(credential: AuthCredential): DefaultResult<UiText>
}