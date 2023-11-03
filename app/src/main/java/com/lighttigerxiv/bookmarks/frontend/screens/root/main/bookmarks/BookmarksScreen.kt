package com.lighttigerxiv.bookmarks.frontend.screens.root.main.bookmarks

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.backend.utils.getFaviconUrl
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.HorizontalSpacer
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.SpacerSize
import com.lighttigerxiv.bookmarks.frontend.composables.TextField
import com.lighttigerxiv.bookmarks.frontend.composables.VerticalSpacer
import com.lighttigerxiv.bookmarks.frontend.navigation.openBookmark
import kotlinx.coroutines.flow.collect
import org.jetbrains.annotations.Async

@Composable
fun BookmarksScreen(
    rootController: NavHostController
) {

    val appVM: AppVM = viewModel()
    val vm: BookmarksScreenVM = viewModel()

    val screenInitialized = vm.screenInitialized.collectAsState().value
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
            Row {

                TextField(
                    text = searchText,
                    onTextChange = {
                        vm.updateSearchText(it)
                        vm.filterBookmarks(bookmarks)
                    },
                    placeholder = "Search Bookmarks",
                    startIcon = R.drawable.search,
                    onStartIconClick = {}
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

@Composable
fun BookmarkCard(
    bookmark: Bookmark,
    vm: BookmarksScreenVM,
    rootController: NavHostController
) {

    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                vm.openUrl(bookmark.url)
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
                .clip(CircleShape)
                .clickable { showMenu = true },
            painter = painterResource(id = R.drawable.vertical_dots),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )

        Column {
            BookmarkMenu(
                vm = vm,
                rootController = rootController,
                showMenu = showMenu,
                bookmark = bookmark,
                onDismiss = { showMenu = false }
            )
        }
    }
}

@Composable
fun BookmarkMenu(
    vm: BookmarksScreenVM,
    rootController: NavHostController,
    showMenu: Boolean,
    bookmark: Bookmark,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { onDismiss() }
    ) {

        DropdownMenuItem(
            text = {
                NormalText(text = "Edit")
            },
            onClick = {
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
                vm.copyUrl(url = bookmark.url)
                onDismiss()
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