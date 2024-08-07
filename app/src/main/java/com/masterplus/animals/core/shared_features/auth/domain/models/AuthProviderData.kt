package com.masterplus.animals.core.shared_features.auth.domain.models

import android.net.Uri
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType

data class AuthProviderData(
    val providerType: AuthProviderType,
    val displayName: String?,
    val email: String?,
    val photoUrl: Uri?
)
