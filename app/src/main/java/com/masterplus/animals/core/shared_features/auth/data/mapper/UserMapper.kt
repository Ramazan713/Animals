package com.masterplus.animals.core.shared_features.auth.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.masterplus.animals.core.shared_features.auth.domain.models.User

fun FirebaseUser.toUser(): User {
    return User(
        uid = uid,
        name = displayName,
        email = email,
        photoUri = photoUrl
    )
}