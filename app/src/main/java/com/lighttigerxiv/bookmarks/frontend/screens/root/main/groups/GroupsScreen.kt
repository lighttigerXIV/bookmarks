package com.lighttigerxiv.bookmarks.frontend.screens.root.main.groups

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.R
import com.lighttigerxiv.bookmarks.backend.realm.objects.Group
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.HorizontalSpacer
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.SpacerSize
import com.lighttigerxiv.bookmarks.frontend.composables.TextField
import com.lighttigerxiv.bookmarks.frontend.composables.VerticalSpacer
import com.lighttigerxiv.bookmarks.frontend.navigation.openGroup

@Composable
fun GroupsScreen(
    rootController: NavHostController,
    appVM: AppVM
) {

    val vm: GroupsScreenVM = viewModel()

    val screenInitialized = vm.screenInitialized.collectAsState().value
    val settings = appVM.settings.collectAsState().value
    val requestedFocusSearch = appVM.requestedFocusSearch.collectAsState().value
    val groups = appVM.groups.collectAsState().value
    val searchText = vm.searchText.collectAsState().value
    val currentGroups = vm.currentGroups.collectAsState().value

    if (!screenInitialized) {
        vm.initScreen(groups)
    }

    LaunchedEffect(groups) {
        vm.filterGroups(groups)
    }


    Column {

        if (screenInitialized) {

            if (groups.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    NormalText(text = "No groups found.")
                }
            } else {
                TextField(
                    text = searchText,
                    onTextChange = {
                        vm.updateSearchText(it)
                        vm.filterGroups(groups)
                    },
                    placeholder = "Search Groups",
                    startIcon = R.drawable.search,
                    requestFocus = !requestedFocusSearch && settings!!.searchOnOpen,
                    onFocusRequested = { appVM.updateRequestedFocusSearch(true) }
                )

                VerticalSpacer(size = SpacerSize.Large)

                LazyColumn {
                    items(
                        items = currentGroups,
                        key = { it._id.toHexString() }
                    ) { group ->

                        Column {

                            GroupCard(group, vm, rootController)

                            VerticalSpacer(SpacerSize.Small)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroupCard(
    group: Group,
    vm: GroupsScreenVM,
    rootController: NavHostController
) {

    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { vm.openGroup(group) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .clickable { showMenu = true },
            painter = painterResource(id = R.drawable.folder_outline),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )

        HorizontalSpacer(size = SpacerSize.Medium)

        NormalText(text = group.name)

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f, fill = true))

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
            GroupMenu(
                rootController = rootController,
                showMenu = showMenu,
                group = group,
                onDismiss = { showMenu = false }
            )
        }
    }
}

@Composable
fun GroupMenu(
    rootController: NavHostController,
    showMenu: Boolean,
    group: Group,
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
                onDismiss()
                rootController.openGroup(group._id)
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = null
                )
            }
        )
    }
}