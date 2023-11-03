package com.lighttigerxiv.bookmarks.frontend.composables

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NormalText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    fontSize: TextUnit = 14.sp,
    offset: Dp = 0.dp
){

    Text(
        modifier = Modifier.offset(x = offset),
        text = text,
        fontSize = fontSize,
        color = color,
        fontWeight = FontWeight.Medium
    )
}