package com.masterplus.animals.core.shared_features.auth.presentation.utils

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.masterplus.animals.BuildConfig
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.ErrorText
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.UiText
import kotlinx.coroutines.tasks.await

object AuthProviderUtils {

    suspend fun reAuthenticateWithFirebaseProvider(
        context: Context,
        oAuthProvider: OAuthProvider
    ): DefaultResult<AuthCredential>{
        val firebaseAuth = FirebaseAuth.getInstance()
        try{
            val result = firebaseAuth.currentUser?.startActivityForReauthenticateWithProvider(context as Activity,oAuthProvider)?.await()
            return Result.Success(result?.credential!!)
        }catch (e: Exception){
            return Result.Error(ErrorText(UiText.Text(e.localizedMessage ?: "Error")))
        }
    }

    suspend fun linkWithFirebaseProvider(
        context: Context,
        oAuthProvider: OAuthProvider
    ): DefaultResult<AuthCredential>{
        val firebaseAuth = FirebaseAuth.getInstance()
        try{
            val result = firebaseAuth.currentUser?.startActivityForLinkWithProvider(context as Activity,oAuthProvider)?.await()
            return Result.Success(result?.credential!!)
        }catch (e: Exception){
            return Result.Error(ErrorText(UiText.Text(e.localizedMessage ?: "Error")))
        }
    }

    suspend fun signInWithFirebaseProvider(
        context: Context,
        oAuthProvider: OAuthProvider
    ): DefaultResult<AuthCredential>{
        val firebaseAuth = FirebaseAuth.getInstance()
        try{
            val result = firebaseAuth.startActivityForSignInWithProvider(context as Activity,oAuthProvider).await()
            return Result.Success(result.credential!!)
        }catch (e: Exception){
            return Result.Error(ErrorText(UiText.Text(e.localizedMessage ?: "Error")))
        }
    }

    suspend fun signInWithGoogle(
        credentialManager: CredentialManager,
        context: Context
    ): DefaultResult<AuthCredential>{
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.AUTH_CLIENT_ID)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        try{
            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )
            when(val credential = result.credential){
                is CustomCredential -> {
                    try {
                        if(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                            val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                            return Result.Success(authCredential)
                        }
                    }catch (e: GoogleIdTokenParsingException){
                        return Result.Error(ErrorText(UiText.Text(e.localizedMessage ?: "Error")))
                    }
                }
            }

        }catch (e: GetCredentialException){
            return Result.Error(ErrorText(UiText.Text(e.localizedMessage ?: "Error")))
        }
        return Result.Error(ErrorText(UiText.Resource(R.string.something_went_wrong)))
    }
}