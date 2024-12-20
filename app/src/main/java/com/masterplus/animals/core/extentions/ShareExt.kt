package com.masterplus.animals.core.extentions

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.utils.ToastHelper


fun String.shareText(context: Context){
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, this@shareText)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

fun String.copyClipboardText(clipboardManager: ClipboardManager){
    clipboardManager.setText(buildAnnotatedString { append(this@copyClipboardText) })
}

fun Context.shareApp(){
    val url = "http://play.google.com/store/apps/details?id=$packageName"
    url.shareText(this)
}

fun Context.launchUrl(url: String){
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        setPackage("com.android.vending")
    }
    startActivity(intent)
}
fun Context.launchPlayApp(
    onError: ((UiText) -> Unit)? = null
){
    try {
        val url = "http://play.google.com/store/apps/details?id=$packageName"
        launchUrl(url)
    }catch (e: Exception){
        onError?.invoke(UiText.Resource(R.string.something_went_wrong))
    }
}

