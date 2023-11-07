package com.lighttigerxiv.bookmarks.frontend.screens.root.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.HorizontalSpacer
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.SpacerSize
import com.lighttigerxiv.bookmarks.frontend.composables.Toolbar
import com.lighttigerxiv.bookmarks.frontend.composables.VerticalSpacer

@Composable
fun SettingsScreen(
    appVM: AppVM,
    rootController: NavHostController
) {

    val settings = appVM.settings.collectAsState().value

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Toolbar(controller = rootController)

        VerticalSpacer(size = SpacerSize.Large)

        if(settings != null){

            ToggleSettingCard(
                checked = settings.openGroupsByDefault,
                onCheckChange = { appVM.updateOpenGroupsByDefault(it) },
                title = "Open Groups By Default",
                description = "Opens the groups tab instead of bookmarks when opening the app"
            )
            
            VerticalSpacer(size = SpacerSize.Small)

            ToggleSettingCard(
                checked = settings.searchOnOpen,
                onCheckChange = { appVM.updateSearchOnOpen(it) },
                title = "Search On Open",
                description = "Focus on the search bar when opening the app"
            )
        }
    }
}


@Composable
fun ToggleSettingCard(
    checked: Boolean,
    onCheckChange: (checked: Boolean) -> Unit,
    title: String,
    description: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onCheckChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true)
        ) {

            NormalText(text = title, fontSize = 18.sp)
            
            VerticalSpacer(size = SpacerSize.Small)

            NormalText(text = description)
        }
        
        HorizontalSpacer(size = SpacerSize.Small)

        Switch(checked = checked, onCheckedChange = { onCheckChange(!checked) })
    }
}