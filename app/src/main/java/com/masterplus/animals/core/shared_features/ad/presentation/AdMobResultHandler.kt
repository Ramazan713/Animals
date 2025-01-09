package com.masterplus.animals.core.shared_features.ad.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.utils.EventHandler
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import kotlin.math.acos


@Composable
fun AdMobResultHandler(
    adUiResult: AdUiResult?,
    onAdAction: (AdAction) -> Unit,
    label: String,
    showErrorMessage: Boolean = false,
    onSafeAdResult: (AdUiResult) -> Unit
) {
    var message by remember {
        mutableStateOf<UiText?>(null)
    }

    ShowLifecycleToastMessage(
        message = message
    ) {
        message = null
    }

    EventHandler(adUiResult) { adResult ->
        when(adResult){
            is AdUiResult.OnShowingRewardSuccess -> {
                if(adResult.label == label){
                    onAdAction(AdAction.ClearUiResult)
                    onSafeAdResult(adResult)
                }
            }
            is AdUiResult.OnShowingRewardFailed -> {
                if(adResult.label == label){
                    onAdAction(AdAction.ClearUiResult)
                    if(showErrorMessage){
                        message = adResult.error
                    }
                    onSafeAdResult(adResult)
                }
            }
        }
    }
}