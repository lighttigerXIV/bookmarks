package com.lighttigerxiv.bookmarks.frontend.screens.root.main.groups

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.composables.NormalText
import com.lighttigerxiv.bookmarks.frontend.composables.TextField

@Composable
fun GroupsScreen(){

    val appVM: AppVM = viewModel()
    val vm: GroupsScreenVM = viewModel()

    val screenInitialized = vm.screenInitialized.collectAsState().value
    val searchText = vm.searchText.collectAsState().value
    val groups = appVM.groups.collectAsState().value
    val currentGroups = vm.currentGroups.collectAsState().value

    if(!screenInitialized){
        vm.initScreen(groups)
    }

    LaunchedEffect(groups){
        vm.filterGroups(groups)
    }


    Column {

        if(screenInitialized){

            TextField(
                text = searchText,
                onTextChange = {vm.updateSearchText(it)},
                placeholder = "Search Groups"
            )

            LazyColumn{
                items(
                    items = currentGroups,
                    key = { it._id.toHexString() }
                ){ group ->

                    NormalText(text = group.name)
                }
            }
        }

    }
}