package com.masterplus.animals.core.shared_features.auth.domain.models

import android.net.Uri

data class User(
    val uid: String,
    val email: String?,
    val photoUri: Uri?,
    val name: String?,
    val authProviders: List<AuthProviderData> = emptyList()
)
