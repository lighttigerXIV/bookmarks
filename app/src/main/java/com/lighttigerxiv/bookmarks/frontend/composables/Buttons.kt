package com.lighttigerxiv.bookmarks.frontend.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun DangerButton(
    text: String,
    disabled: Boolean = false,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (disabled) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.error)
            .modifyIf(!disabled){
                clickable { if (!disabled) onClick() }
            }
            .padding(8.dp)
            .padding(start = 16.dp, end = 16.dp)
    ) {

        NormalText(
            text = text,
            color = if (disabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onError
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    disabled: Boolean = false,
    fillWidth: Boolean = false,
    onClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .modifyIf(fillWidth){
                fillMaxWidth()
            }
            .clip(CircleShape)
            .border(1.dp, if (disabled) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary, CircleShape)
            .modifyIf(!disabled){
                clickable { if (!disabled) onClick() }
            }
            .padding(8.dp)
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {

        NormalText(
            text = text,
            color = if (disabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
        )
    }
}