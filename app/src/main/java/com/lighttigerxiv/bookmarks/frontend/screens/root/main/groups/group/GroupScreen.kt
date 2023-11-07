package com.lighttigerxiv.bookmarks.frontend.screens.root.main.groups.group

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.backend.utils.getFaviconUrl
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.DangerButton
import com.lighttigerxiv.bookmarks.frontend.composables.EditToolbar
import com.lighttigerxiv.bookmarks.frontend.composables.HorizontalSpacer
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.SecondaryButton
import com.lighttigerxiv.bookmarks.frontend.composables.SpacerSize
import com.lighttigerxiv.bookmarks.frontend.composables.TextField
import com.lighttigerxiv.bookmarks.frontend.composables.VerticalSpacer
import org.mongodb.kbson.ObjectId

@Composable
fun GroupScreen(
    id: ObjectId,
    appVM: AppVM,
    rootController: NavHostController
) {

    val vm: GroupScreenVM = viewModel()

    val screenInitialized = vm.screenInitialized.collectAsState().value
    val showDeleteDialog = vm.showDeleteDialog.collectAsState().value
    val bookmarks = appVM.bookmarks.collectAsState().value
    val groups = appVM.groups.collectAsState().value
    val group = vm.group.collectAsState().value
    val groupBookmarks = vm.groupBookmarks.collectAsState().value
    val showAddBookmarkDialog = vm.showAddBookmarkDialog.collectAsState().value
    val name = vm.name.collectAsState().value


    if (!screenInitialized) {
        vm.initScreen(id, bookmarks, groups)
    }


    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {


        EditToolbar(
            showContent = screenInitialized,
            controller = rootController,
            onDeleteClick = { vm.updateShowDeleteDialog(true) },
            onSaveClick = { vm.updateGroup(id, rootController) },
            disableSaveButton = name.isEmpty()
        )

        if (screenInitialized) {

            LazyColumn {

                item {

                    NormalText(text = "Name", offset = 8.dp)

                    TextField(
                        text = name,
                        onTextChange = { vm.updateName(it) },
                        placeholder = "Group Name"
                    )

                    VerticalSpacer(size = SpacerSize.Large)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        NormalText(text = "Bookmarks", offset = 8.dp)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f, fill = true)
                        )

                        if (bookmarks.isNotEmpty()) {
                            SecondaryButton(text = "Add") { vm.updateShowAddBookmarkDialog(true) }
                        }
                    }

                    VerticalSpacer(size = SpacerSize.Small)

                    if (bookmarks.isEmpty()) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            NormalText(text = "No bookmarks found.")
                        }
                    }
                }

                items(
                    items = groupBookmarks,
                    key = { it._id.toHexString() }
                ) { bookmark ->

                    Column {

                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            AsyncImage(
                                modifier = Modifier.size(30.dp),
                                model = getFaviconUrl(bookmark.url),
                                contentDescription = null
                            )

                            HorizontalSpacer(size = SpacerSize.Medium)

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f, fill = true)
                            ) {
                                NormalText(text = bookmark.name)
                                NormalText(text = bookmark.url, fontSize = 12.sp)
                            }

                            Icon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .clickable { vm.removeBookmark(bookmark) },
                                painter = painterResource(id = R.drawable.remove),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        VerticalSpacer(size = SpacerSize.Small)
                    }
                }
            }
        }

        DeleteDialog(showDeleteDialog, id, vm, rootController)


        AddBookmarksDialog(showAddBookmarkDialog, bookmarks, vm)
    }
}

@Composable
fun DeleteDialog(
    show: Boolean,
    id: ObjectId,
    vm: GroupScreenVM,
    rootController: NavHostController
) {

    if (show) {

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

                    NormalText(text = "Are you sure you want to delete this group?")

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
                                vm.deleteGroup(id, rootController)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AddBookmarksDialog(
    show: Boolean,
    bookmarks: List<Bookmark>,
    vm: GroupScreenVM
) {

    if (show) {
        Dialog(onDismissRequest = { vm.updateShowAddBookmarkDialog(false) }) {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp)
            ) {

                Column {

                    NormalText(text = "Add Bookmark", fontSize = 20.sp)

                    VerticalSpacer(size = SpacerSize.Large)

                    LazyColumn {
                        items(
                            items = bookmarks,
                            key = { "add_bookmark_${it._id.toHexString()}" }
                        ) { bookmark ->
                            Column {

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable { vm.addBookmark(bookmark) }
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    AsyncImage(
                                        modifier = Modifier.size(30.dp),
                                        model = getFaviconUrl(bookmark.url),
                                        contentDescription = null
                                    )

                                    HorizontalSpacer(size = SpacerSize.Medium)

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f, fill = true)
                                    ) {
                                        NormalText(text = bookmark.name)
                                        NormalText(text = bookmark.url, fontSize = 12.sp)
                                    }
                                }

                                VerticalSpacer(size = SpacerSize.Small)
                            }
                        }
                    }

                    VerticalSpacer(size = SpacerSize.Large)

                    SecondaryButton(
                        text = "Cancel",
                        fillWidth = true
                    ) {
                        vm.updateShowAddBookmarkDialog(false)
                    }
                }
            }
        }
    }
}