package com.lighttigerxiv.bookmarks.frontend.screens.root.main.groups

import androidx.lifecycle.ViewModel
import com.lighttigerxiv.bookmarks.backend.realm.objects.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GroupsScreenVM: ViewModel() {

    private val _screenInitialized = MutableStateFlow(false)
    val screenInitialized = _screenInitialized.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    fun updateSearchText(v: String) {
        _searchText.update { v }
    }

    private val _currentGroups = MutableStateFlow<List<Group>>(ArrayList())
    val currentGroups = _currentGroups.asStateFlow()

    fun initScreen(groups: List<Group>){

        _currentGroups.update { groups }
        _screenInitialized.update { true }
    }

    fun filterGroups(groups: List<Group>){
        val newList = groups.filter {
            it.name.trim().lowercase().contains(searchText.value.trim().lowercase())
        }

        _currentGroups.update { newList }
    }
}