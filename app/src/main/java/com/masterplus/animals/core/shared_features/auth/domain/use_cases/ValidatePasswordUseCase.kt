package com.masterplus.animals.core.shared_features.auth.domain.use_cases

import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText

class ValidatePasswordUseCase {
    operator fun invoke(password: String?): UiText?{
        if(password == null) return null
        if(password.isBlank()){
            return UiText.Resource(R.string.password_field_can_not_be_empty)
        }
        if(password.length <= 6){
            return UiText.Resource(R.string.password_field_must_be_at_least_seven_size)
        }
        return null
    }
}