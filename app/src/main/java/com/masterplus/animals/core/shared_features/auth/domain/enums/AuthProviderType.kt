package com.masterplus.animals.core.shared_features.auth.domain.enums

import com.masterplus.animals.core.domain.utils.UiText

enum class AuthProviderType(
    val providerId: String,
    val title: UiText
) {
    Email(
        providerId = "password",
        title = UiText.Text("Email")
    ),
    Google(
        providerId = "google.com",
        title = UiText.Text("Google")
    ),
    X(
        providerId = "twitter.com",
        title = UiText.Text("X")
    );

    val isEmail get() = this == Email
    val isGoogle get() = this == Google
    val isX get() = this == X

    companion object {
        fun fromProviderId(providerId: String): AuthProviderType?{
            AuthProviderType.entries.forEach { type ->
                if(type.providerId == providerId) {
                    return type
                }
            }
            return null
        }
    }
}