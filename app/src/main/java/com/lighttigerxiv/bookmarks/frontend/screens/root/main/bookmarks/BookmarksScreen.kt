package com.lighttigerxiv.bookmarks.frontend.screens.root.main.bookmarks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.backend.utils.getFaviconUrl
import com.lighttigerxiv.bookmarks.backend.utils.openUrl
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.HorizontalSpacer
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.SpacerSize
import com.lighttigerxiv.bookmarks.frontend.composables.TextField
import com.lighttigerxiv.bookmarks.frontend.composables.VerticalSpacer
import com.lighttigerxiv.bookmarks.frontend.navigation.openAbout
import com.lighttigerxiv.bookmarks.frontend.navigation.openBookmark
import com.lighttigerxiv.bookmarks.frontend.navigation.openSettings

@Composable
fun BookmarksScreen(
    rootController: NavHostController,
    appVM: AppVM
) {
    val vm: BookmarksScreenVM = viewModel()

    val settings = appVM.settings.collectAsState().value
    val screenInitialized = vm.screenInitialized.collectAsState().value
    val requestedFocusSearch = appVM.requestedFocusSearch.collectAsState().value
    val bookmarks = appVM.bookmarks.collectAsState().value
    val currentBookmarks = vm.currentBookmarks.collectAsState().value
    val searchText = vm.searchText.collectAsState().value


    if (!screenInitialized) {
        vm.initScreen(bookmarks)
    }

    LaunchedEffect(bookmarks) {
        vm.filterBookmarks(bookmarks)
    }

    Column {

        if (screenInitialized) {
            if (bookmarks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    NormalText(text = "No bookmarks found.")
                }
            } else {

                Row {

                    SearchMenu(vm, rootController)

                    TextField(
                        text = searchText,
                        onTextChange = {
                            vm.updateSearchText(it)
                            vm.filterBookmarks(bookmarks)
                        },
                        placeholder = "Search Bookmarks",
                        startIcon = R.drawable.menu,
                        onStartIconClick = { vm.updateShowSearchMenu(true) },
                        requestFocus = !requestedFocusSearch && settings!!.searchOnOpen,
                        onFocusRequested = { appVM.updateRequestedFocusSearch(true) }
                    )
                }


                VerticalSpacer(size = SpacerSize.Large)

                LazyColumn {
                    items(
                        items = currentBookmarks,
                        key = { it._id.toHexString() }
                    ) { bookmark ->

                        Column {

                            BookmarkCard(bookmark, vm, rootController)

                            VerticalSpacer(size = SpacerSize.Small)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchMenu(
    vm: BookmarksScreenVM,
    rootController: NavHostController
) {

    val show = vm.showSearchMenu.collectAsState().value

    DropdownMenu(expanded = show, onDismissRequest = { vm.updateShowSearchMenu(false) }) {
        DropdownMenuItem(
            text = { NormalText(text = "Settings") },
            onClick = {
                vm.updateShowSearchMenu(false)
                rootController.openSettings()
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        )

        DropdownMenuItem(
            text = { NormalText(text = "About") },
            onClick = {
                vm.updateShowSearchMenu(false)
                rootController.openAbout()
            },
            leadingIcon = {
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}

@Composable
fun BookmarkCard(
    bookmark: Bookmark,
    vm: BookmarksScreenVM,
    rootController: NavHostController
) {

    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                openUrl(context, bookmark.url)
            }
            .padding(16.dp),
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
                .size(20.dp)
                .clip(CircleShape)
                .clickable { showMenu = true },
            painter = painterResource(id = R.drawable.vertical_dots),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Column {
            BookmarkMenu(
                show = showMenu,
                vm = vm,
                rootController = rootController,
                bookmark = bookmark,
                onDismiss = { showMenu = false }
            )
        }
    }
}

@Composable
fun BookmarkMenu(
    show: Boolean,
    vm: BookmarksScreenVM,
    rootController: NavHostController,
    bookmark: Bookmark,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = show,
        onDismissRequest = { onDismiss() }
    ) {

        DropdownMenuItem(
            text = {
                NormalText(text = "Edit")
            },
            onClick = {
                onDismiss()
                rootController.openBookmark(bookmark._id)
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = null
                )
            }
        )

        DropdownMenuItem(
            text = {
                NormalText(text = "Copy url")
            },
            onClick = {
                onDismiss()
                vm.copyUrl(url = bookmark.url)
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.copy),
                    contentDescription = null
                )
            }
        )
    }
}