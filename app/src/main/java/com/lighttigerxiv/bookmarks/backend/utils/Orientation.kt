package com.lighttigerxiv.bookmarks.backend.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun onLandscape(): Boolean{
    return LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
}