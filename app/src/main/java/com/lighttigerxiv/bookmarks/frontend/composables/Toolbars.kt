package com.lighttigerxiv.bookmarks.frontend.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.frontend.navigation.goBack

@Composable
fun Toolbar(
    title: String = "",
    controller: NavHostController
) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        Icon(
            modifier = Modifier.clip(CircleShape).clickable { controller.goBack() }.padding(8.dp),
            painter = painterResource(id = R.drawable.chevron_left),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        HorizontalSpacer(SpacerSize.Medium)

        NormalText(text = title, fontSize = 18.sp)
    }
}