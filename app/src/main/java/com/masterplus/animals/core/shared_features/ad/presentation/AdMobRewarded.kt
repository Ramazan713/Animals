package com.masterplus.animals.core.shared_features.ad.presentation

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.masterplus.animals.BuildConfig

class AdMobRewarded(
    private val context: Context
) {
    private var rewarded: RewardedAd? = null

    fun showOrLoadAd(
        onAdShowed: () -> Unit,
        onAddError: ((String) -> Unit)? = null,
        onAdLoading: (() -> Unit)? = null,
        onAdLoaded: (() -> Unit)? = null
    ){
        if(rewarded != null){
            showAd(rewarded, onAdShowed)
        }else{
            loadAd(
                onAdShowed = onAdShowed,
                onAdLoaded = { ad ->
                    onAdLoaded?.invoke()
                    showAd(
                        rewarded = ad
                    )
                },
                onAddError = onAddError,
                onAdLoading = onAdLoading,
            )
        }
    }

    private fun showAd(
        rewarded: RewardedAd?,
        onAdShowed: (() -> Unit)? = null
    ){
        rewarded?.show(context as Activity, OnUserEarnedRewardListener { rewardItem ->
            onAdShowed?.invoke()
        })
    }

    fun loadAd(
        onAddError: ((String) -> Unit)? = null,
        onAdLoading: (() -> Unit)? = null,
        onAdLoaded: (() -> Unit)? = null
    ){
        loadAd(
            onAddError = onAddError,
            onAdLoading = onAdLoading,
            onAdLoaded = {
                onAdLoaded?.invoke()
            },
            onAdShowed = {}
        )
    }

    private fun loadAd(
        onAdShowed: (() -> Unit)? = null,
        onAdLoaded: ((RewardedAd) -> Unit)? = null,
        onAddError: ((String) -> Unit)? = null,
        onAdLoading: (() -> Unit)? = null,
    ){
        if(rewarded != null) return

        val adRequest = AdRequest.Builder().build()
        onAdLoading?.invoke()

        RewardedAd.load(context, BuildConfig.REWARDED_AD_ID,
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    onAddError?.invoke(adError.message)
                    rewarded = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    rewarded = rewardedAd
                    onAdLoaded?.invoke(rewardedAd)
                    adCallback {
                        onAdShowed?.invoke()
                    }
                }
            })
    }

    private fun adCallback(onAdShowed: () -> Unit){
        rewarded?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewarded = null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                rewarded = null
            }
            override fun onAdShowedFullScreenContent() {
                onAdShowed()
            }
        }
    }
}