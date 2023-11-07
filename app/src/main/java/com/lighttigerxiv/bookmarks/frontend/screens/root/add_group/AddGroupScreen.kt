package com.lighttigerxiv.bookmarks.frontend.screens.root.add_group

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.backend.utils.getFaviconUrl
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.HorizontalSpacer
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.PrimaryButton
import com.lighttigerxiv.bookmarks.frontend.composables.SpacerSize
import com.lighttigerxiv.bookmarks.frontend.composables.TextField
import com.lighttigerxiv.bookmarks.frontend.composables.Toolbar
import com.lighttigerxiv.bookmarks.frontend.composables.VerticalSpacer

@Composable
fun AddGroupScreen(
    rootController: NavHostController,
    appVM: AppVM
) {

    val vm: AddGroupScreenVM = viewModel()

    val name = vm.name.collectAsState().value
    val bookmarks = appVM.bookmarks.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Toolbar(controller = rootController)

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f, fill = true)
        ) {

            item {

                NormalText(
                    text = "Name",
                    offset = 8.dp
                )

                TextField(
                    text = name,
                    onTextChange = { vm.updateName(it) },
                    placeholder = "Group name"
                )

                VerticalSpacer(size = SpacerSize.Large)

                NormalText(
                    text = "Bookmarks",
                    offset = 8.dp
                )
            }

            items(
                items = bookmarks,
                key = { it._id.toHexString() }
            ) { bookmark ->

                Column {

                    BookmarkCard(bookmark, vm)

                    VerticalSpacer(size = SpacerSize.Small)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            PrimaryButton(
                text = "Save",
                onClick = { vm.addGroup(rootController) },
                disabled = name.isEmpty()
            )
        }
    }
}

@Composable
fun BookmarkCard(
    bookmark: Bookmark,
    vm: AddGroupScreenVM
) {

    var isSelected by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                vm.toggleBookmark(bookmark)
                isSelected = vm.isBookmarkSelected(bookmark)
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


        if (isSelected) {

            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        } else {

            Box(
                modifier = Modifier.size(24.dp)
            )
        }
    }
}