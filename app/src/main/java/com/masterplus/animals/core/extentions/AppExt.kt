package com.masterplus.animals.core.extentions

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import com.masterplus.animals.MainActivity


@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
fun Context.refreshApp(){
    val activity = this as Activity
    activity.finish()
    activity.startActivity(Intent(activity, MainActivity::class.java))
}



fun Int.addPrefixZeros(totalLength: Int): String{
    val numStr = this.toString()
    val prefix = (totalLength-numStr.length).let { num->
        if(num>0) "0".repeat(num) else ""
    }
    return prefix + numStr
}