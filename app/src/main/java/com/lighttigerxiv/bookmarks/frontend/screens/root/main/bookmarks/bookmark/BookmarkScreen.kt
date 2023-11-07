package com.lighttigerxiv.bookmarks.frontend.screens.root.main.bookmarks.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.DangerButton
import com.lighttigerxiv.bookmarks.frontend.composables.EditToolbar
import com.lighttigerxiv.bookmarks.frontend.composables.HorizontalSpacer
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.PrimaryButton
import com.lighttigerxiv.bookmarks.frontend.composables.SecondaryButton
import com.lighttigerxiv.bookmarks.frontend.composables.SpacerSize
import com.lighttigerxiv.bookmarks.frontend.composables.TextField
import com.lighttigerxiv.bookmarks.frontend.composables.Toolbar
import com.lighttigerxiv.bookmarks.frontend.composables.VerticalSpacer
import org.mongodb.kbson.ObjectId

@Composable
fun BookmarkScreen(
    id: ObjectId,
    appVM: AppVM,
    rootController: NavHostController
) {

    val bookmarks = appVM.bookmarks.collectAsState().value

    val vm: BookmarkScreenVM = viewModel()
    val dataInitiated = vm.dataInitiated.collectAsState().value
    val showDeleteDialog = vm.showDeleteDialog.collectAsState().value
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


        EditToolbar(
            showContent = dataInitiated,
            controller = rootController,
            onDeleteClick = { vm.updateShowDeleteDialog(true) },
            onSaveClick = { vm.editBookmark(id, rootController, appVM) }
        )

        if (dataInitiated) {

            Column{

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
        }

        if (showDeleteDialog) {
            DeleteDialog(id, vm, rootController)
        }
    }
}

@Composable
fun DeleteDialog(
    id: ObjectId,
    vm: BookmarkScreenVM,
    rootController: NavHostController
) {

    Dialog(
        onDismissRequest = { vm.updateShowDeleteDialog(false) }
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {

            Column {

                NormalText(text = "Delete Bookmark", fontSize = 20.sp)

                VerticalSpacer(size = SpacerSize.Large)

                NormalText(text = "Are you sure you want to delete this bookmark?")

                VerticalSpacer(size = SpacerSize.Large)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    SecondaryButton(text = "Cancel",
                        onClick = {
                            vm.updateShowDeleteDialog(false)
                        }
                    )

                    HorizontalSpacer(size = SpacerSize.Small)

                    DangerButton(text = "Delete",
                        onClick = {
                            vm.updateShowDeleteDialog(false)
                            vm.deleteBookmark(id, rootController)
                        }
                    )
                }
            }
        }
    }
}