package com.lighttigerxiv.bookmarks.backend.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openUrl(context: Context, url: String) {

    var validUrl = url

    if (!url.startsWith("https://") && !url.startsWith("http://")) {
        validUrl = "https://$url"
    }

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}