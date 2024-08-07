package com.masterplus.animals.core.shared_features.auth.data.repo

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.EmptyResult
import com.masterplus.animals.core.domain.utils.ErrorText
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.domain.utils.safeCall
import com.masterplus.animals.core.shared_features.auth.data.mapper.toUser
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType
import com.masterplus.animals.core.shared_features.auth.domain.models.User
import com.masterplus.animals.core.shared_features.auth.domain.repo.AuthRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class FirebaseAuthRepo: AuthRepo {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userChangesFlow = MutableSharedFlow<User?>(replay = 1)

    override fun userFlow(): Flow<User?> {
        val authStateFlow = callbackFlow {
            val authStateListener = AuthStateListener{auth->
                launch { send(auth.currentUser?.toUser()) }
            }
            firebaseAuth.addAuthStateListener(authStateListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }
        return merge(authStateFlow, userChangesFlow).conflate()
    }

    override fun isLogin(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun currentUser(): User? {
        return firebaseAuth.currentUser?.toUser()
    }

    override suspend fun signInWithEmail(email: String, password: String): DefaultResult<User> {
        return safeCallWithFirebase {
            firebaseAuth.signInWithEmailAndPassword(email.trim(), password.trim()).await().user!!.toUser()
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String): DefaultResult<User> {
        return safeCallWithFirebase {
            firebaseAuth.createUserWithEmailAndPassword(email.trim(), password.trim()).await().user!!.toUser()
        }
    }

    override suspend fun signInWithCredential(credential: AuthCredential): DefaultResult<User> {
        return firebaseCredentialSignIn(credential)
    }

    override suspend fun linkWithCredential(credential: AuthCredential): EmptyResult<ErrorText> {
        return safeCallWithFirebase {
            firebaseAuth.currentUser!!.linkWithCredential(credential).await()?.let { user ->
                userChangesFlow.emit(user.user?.toUser())
            }
        }
    }

    override suspend fun unLinkWith(providerType: AuthProviderType): EmptyResult<ErrorText> {
        return safeCallWithFirebase {
            firebaseAuth.currentUser!!.unlink(providerType.providerId).await()?.let { user ->
                userChangesFlow.emit(user.user?.toUser())
            }
        }
    }

    override suspend fun resetPassword(email: String): DefaultResult<UiText> {
        return safeCallWithFirebase {
            firebaseAuth.sendPasswordResetEmail(email.trim()).await()
            return@safeCallWithFirebase UiText.Resource(R.string.email_send_for_reset_password)
        }
    }



    override suspend fun signOut(): DefaultResult<UiText> {
        return safeCallWithFirebase {
            firebaseAuth.signOut()
            UiText.Resource(R.string.successfully_log_out)
        }
    }

    override suspend fun deleteUser(credential: AuthCredential): DefaultResult<UiText> {
        return safeCallWithFirebase {
            firebaseAuth.currentUser?.let { user->
                val result = user.reauthenticateAndRetrieveData(credential).await()
                if(result != null){
                    result.user?.delete()?.await()
                    return@safeCallWithFirebase UiText.Resource(R.string.delete_account_success)
                }
            }
            throw Exception()
        }
    }

    private suspend fun firebaseCredentialSignIn(credential: AuthCredential): DefaultResult<User> {
        return safeCallWithFirebase {
            firebaseAuth.signInWithCredential(credential).await().user!!.toUser()
        }
    }


    private suspend inline fun <reified T> safeCallWithFirebase(
        crossinline execute: suspend () -> T,
    ): DefaultResult<T>{
        return safeCall(
            execute = execute,
            onException = { e ->
                val error = e.localizedMessage?.let { UiText.Text(it) } ?: UiText.Resource(R.string.something_went_wrong)
                return Result.errorWithUiText(error)
            },

        )
    }
}