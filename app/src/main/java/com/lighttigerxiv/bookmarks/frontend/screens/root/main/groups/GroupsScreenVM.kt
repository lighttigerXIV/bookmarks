package com.lighttigerxiv.bookmarks.frontend.screens.root.main.groups

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.bookmarks.backend.realm.Queries
import com.lighttigerxiv.bookmarks.backend.realm.getRealm
import com.lighttigerxiv.bookmarks.backend.realm.objects.Group
import com.lighttigerxiv.bookmarks.backend.utils.openUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class GroupsScreenVM(application: Application) : AndroidViewModel(application) {

    val context = application

    private val _screenInitialized = MutableStateFlow(false)
    val screenInitialized = _screenInitialized.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    fun updateSearchText(v: String) {
        _searchText.update { v }
    }

    private val _currentGroups = MutableStateFlow<List<Group>>(ArrayList())
    val currentGroups = _currentGroups.asStateFlow()

    fun initScreen(groups: List<Group>) {

        _currentGroups.update { groups }
        _screenInitialized.update { true }
    }

    fun filterGroups(groups: List<Group>) {
        val newList = groups.filter {
            it.name.trim().lowercase().contains(searchText.value.trim().lowercase())
        }

        _currentGroups.update { newList }
    }

    fun openGroup(group: Group) {

        viewModelScope.launch(Dispatchers.Main) {

            val bookmarks = Queries(getRealm()).getBookmarks()

            for (bookmark in group.bookmarks) {
                openUrl(context, bookmarks.first { it._id == bookmark }.url)
            }
        }

    }
}