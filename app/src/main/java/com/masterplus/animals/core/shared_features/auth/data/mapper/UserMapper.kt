package com.masterplus.animals.core.shared_features.auth.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType
import com.masterplus.animals.core.shared_features.auth.domain.models.AuthProviderData
import com.masterplus.animals.core.shared_features.auth.domain.models.User

fun FirebaseUser.toUser(): User {
    return User(
        uid = uid,
        name = displayName,
        email = email,
        photoUri = photoUrl,
        authProviders = providerData?.mapNotNull { it.toProviderData() } ?: emptyList()
    )
}

fun UserInfo.toProviderData(): AuthProviderData?{
    return AuthProviderData(
        providerType = AuthProviderType.fromProviderId(providerId) ?: return null,
        photoUrl = photoUrl,
        email = email,
        displayName = displayName
    )
}
