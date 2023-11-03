package com.lighttigerxiv.bookmarks.frontend.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class SpacerSize{
    Small,
    Medium,
    Large
}

@Composable
fun HorizontalSpacer(size: SpacerSize){

    val width = when(size){
        SpacerSize.Small -> 8.dp
        SpacerSize.Medium -> 12.dp
        SpacerSize.Large -> 16.dp
    }

    Box(modifier = Modifier.width(width))
}

@Composable
fun VerticalSpacer(size: SpacerSize){

    val height = when(size){
        SpacerSize.Small -> 8.dp
        SpacerSize.Medium -> 12.dp
        SpacerSize.Large -> 16.dp
    }

    Box(modifier = Modifier.height(height))
}