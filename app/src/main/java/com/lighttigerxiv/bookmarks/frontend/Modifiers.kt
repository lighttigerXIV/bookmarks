package com.lighttigerxiv.bookmarks.frontend

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.modifyIf(condition: Boolean, modifier: @Composable Modifier.() -> Modifier) =
    then(if (condition) modifier.invoke(this) else this)