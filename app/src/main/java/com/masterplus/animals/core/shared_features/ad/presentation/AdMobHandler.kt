package com.masterplus.animals.core.shared_features.ad.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.masterplus.animals.core.presentation.utils.EventHandler

@Composable
fun AdMobHandler(
    adUiEventState: AdUiEvent?,
    onAdAction: (AdAction) -> Unit
){
    val context = LocalContext.current
    val adMobInterstitial = remember { AdMobInterstitial(context) }
    val adMobRewarded = remember { AdMobRewarded(context) }

    EventHandler(event = adUiEventState) { event->
        onAdAction(AdAction.ClearUiEvent)
        when(event){
            is AdUiEvent.CheckInterstitialAdShowStatus -> {
                adMobInterstitial.showAd()
            }
            AdUiEvent.LoadInterstitialAd -> {
                adMobInterstitial.loadAd {
                    onAdAction(AdAction.ResetCounter)
                }
            }
            is AdUiEvent.LoadRewordedAd -> {
                adMobRewarded.loadAd()
            }
            is AdUiEvent.ShowRewordedAd -> {
                adMobRewarded.showOrLoadAd(
                    onAdShowed = {
                        onAdAction(AdAction.OnSuccessShowingRewardAd(event.label))
                    },
                    onAddError = {
                        onAdAction(AdAction.OnRewardAdError(it))
                    },
                    onAdLoaded = {
                        onAdAction(AdAction.OnRewardAdLoaded)
                    },
                    onAdLoading = {
                        onAdAction(AdAction.OnRewardAdLoading)
                    }
                )
            }
        }
    }
}