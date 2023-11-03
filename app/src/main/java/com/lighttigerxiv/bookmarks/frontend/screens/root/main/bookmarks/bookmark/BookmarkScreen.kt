package com.lighttigerxiv.bookmarks.frontend.screens.root.main.bookmarks.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.PrimaryButton
import com.lighttigerxiv.bookmarks.frontend.composables.TextField
import com.lighttigerxiv.bookmarks.frontend.composables.Toolbar
import org.mongodb.kbson.ObjectId

@Composable
fun BookmarkScreen(
    id: ObjectId,
    rootController: NavHostController
) {

    val appVM: AppVM = viewModel()
    val bookmarks = appVM.bookmarks.collectAsState().value

    val vm: BookmarkScreenVM = viewModel()
    val dataInitiated = vm.dataInitiated.collectAsState().value
    val name = vm.name.collectAsState().value
    val url = vm.url.collectAsState().value

    if (!dataInitiated) {
        vm.initScreen(id, bookmarks)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        Toolbar(controller = rootController)

        if (dataInitiated) {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f, fill = true)
            ) {

                NormalText(
                    text = "Name",
                    offset = 8.dp
                )

                TextField(
                    text = name,
                    onTextChange = { vm.updateName(it) }
                )

                NormalText(
                    text = "Url",
                    offset = 8.dp
                )

                TextField(
                    text = url,
                    onTextChange = { vm.updateUrl(it) }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                PrimaryButton(
                    text = "Save",
                    onClick = { vm.editBookmark(id, rootController, appVM) },
                    disabled = name.isEmpty() || url.isEmpty()
                )
            }
        }
    }
}