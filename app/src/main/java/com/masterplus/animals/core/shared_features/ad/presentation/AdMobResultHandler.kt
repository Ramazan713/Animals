package com.masterplus.animals.core.shared_features.ad.presentation

import androidx.compose.runtime.Composable
import com.masterplus.animals.core.presentation.utils.EventHandler


@Composable
fun AdMobResultHandler(
    adUiResult: AdUiResult?,
    onAdAction: (AdAction) -> Unit,
    label: String,
    onSafeAdResult: (AdUiResult) -> Unit
) {
    EventHandler(adUiResult) { adResult ->
        when(adResult){
            is AdUiResult.OnShowingRewardSuccess -> {
                if(adResult.label == label){
                    onAdAction(AdAction.ClearUiResult)
                    onSafeAdResult(adResult)
                }
            }
        }
    }
}