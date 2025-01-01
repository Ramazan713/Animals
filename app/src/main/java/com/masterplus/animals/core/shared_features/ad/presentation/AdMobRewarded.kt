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
        onAdShowed: () -> Unit
    ){
        if(rewarded != null){
            showAd(rewarded, onAdShowed)
        }else{
            loadAd(
                onAdShowed = onAdShowed,
                onAddLoaded = {ad ->
                    showAd(
                        rewarded = ad,
                        onAdShowed = onAdShowed
                    )
                }
            )
        }
    }

    private fun showAd(
        rewarded: RewardedAd?,
        onAdShowed: () -> Unit
    ){
        rewarded?.show(context as Activity, OnUserEarnedRewardListener { rewardItem ->
            onAdShowed()
        })
    }

    fun loadAd(
        onAdShowed: () -> Unit
    ){
        loadAd(onAdShowed = onAdShowed, onAddLoaded = {})
    }

    private fun loadAd(
        onAdShowed: () -> Unit,
        onAddLoaded: ((RewardedAd) -> Unit)? = null,
    ){
        if(rewarded != null) return

        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context, BuildConfig.REWARDED_AD_ID,
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewarded = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    rewarded = rewardedAd
                    onAddLoaded?.invoke(rewardedAd)
                    adCallback {
                        onAdShowed()
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