package com.lighttigerxiv.bookmarks.backend.utils

import android.os.Build

fun isAndroid13OrHigher(): Boolean{
    return Build.VERSION.SDK_INT >= 33
}