package com.lighttigerxiv.bookmarks.frontend.screens.root.add_bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.PrimaryButton
import com.lighttigerxiv.bookmarks.frontend.composables.SpacerSize
import com.lighttigerxiv.bookmarks.frontend.composables.TextField
import com.lighttigerxiv.bookmarks.frontend.composables.Toolbar
import com.lighttigerxiv.bookmarks.frontend.composables.VerticalSpacer
import com.lighttigerxiv.bookmarks.frontend.navigation.goBack
import com.lighttigerxiv.bookmarks.frontend.navigation.openMain


@Composable
fun AddBookmarkScreen(
    rootController: NavHostController
) {

    val vm: AddBookmarkScreenVM = viewModel()
    val name = vm.name.collectAsState().value
    val url = vm.url.collectAsState().value


    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, fill = true)
        ) {

            Toolbar(controller = rootController)

            VerticalSpacer(size = SpacerSize.Medium)

            NormalText(text = "Name", offset = 8.dp)

            TextField(
                text = name,
                onTextChange = { vm.updateName(it) },
                placeholder = "The bookmark name",
                hasFollowingField = true
            )

            VerticalSpacer(size = SpacerSize.Small)

            NormalText(text = "Url", offset = 8.dp)

            TextField(
                text = url,
                onTextChange = { vm.updateUrl(it) },
                placeholder = "The bookmark url"
            )
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            PrimaryButton(
                text = "Save",
                disabled = vm.isSaveButtonDisabled(),
                onClick = {
                    vm.addBookmark()
                    rootController.openMain()
                }
            )
        }
    }
}