package com.lighttigerxiv.bookmarks.frontend.screens.root.about

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.backend.utils.openUrl
import com.lighttigerxiv.bookmarks.frontend.composables.HorizontalSpacer
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.SpacerSize
import com.lighttigerxiv.bookmarks.frontend.composables.Toolbar
import com.lighttigerxiv.bookmarks.frontend.composables.VerticalSpacer

@Composable
fun AboutScreen(
    rootController: NavHostController
) {

    val context = LocalContext.current
    val appVersion =
        remember { context.packageManager.getPackageInfo(context.packageName, 0).versionName }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        Toolbar(controller = rootController)

        VerticalSpacer(size = SpacerSize.Large)

        NormalText(text = "Version", offset = 8.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {

            NormalText(text = appVersion)
        }

        VerticalSpacer(size = SpacerSize.Medium)

        NormalText(text = "Source Code", offset = 8.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { openUrl(context, "https://github.com/lighttigerXIV/bookmarks") }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.github),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )

            HorizontalSpacer(size = SpacerSize.Small)

            NormalText(text = "GitHub")
        }
    }
}