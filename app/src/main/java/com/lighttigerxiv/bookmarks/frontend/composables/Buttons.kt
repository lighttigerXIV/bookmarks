package com.lighttigerxiv.bookmarks.frontend.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lighttigerxiv.bookmarks.frontend.modifyIf

@Composable
fun PrimaryButton(
    text: String,
    disabled: Boolean = false,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (disabled) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary)
            .modifyIf(!disabled){
                clickable { if (!disabled) onClick() }
            }
            .padding(12.dp)
            .padding(start = 16.dp, end = 16.dp)
    ) {

        NormalText(
            text = text,
            color = if (disabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
        )
    }
}