package com.lighttigerxiv.bookmarks.frontend.screens.root.main.groups.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.backend.realm.Queries
import com.lighttigerxiv.bookmarks.backend.realm.getRealm
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.backend.realm.objects.Group
import com.lighttigerxiv.bookmarks.frontend.navigation.openMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class GroupScreenVM : ViewModel() {

    private val _screenInitialized = MutableStateFlow(false)
    val screenInitialized = _screenInitialized.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog = _showDeleteDialog.asStateFlow()
    fun updateShowDeleteDialog(v: Boolean) {
        _showDeleteDialog.update { v }
    }

    private val _group = MutableStateFlow<Group?>(null)
    val group = _group.asStateFlow()

    private val _groupBookmarks = MutableStateFlow<List<Bookmark>>(ArrayList())
    val groupBookmarks = _groupBookmarks.asStateFlow()


    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    fun updateName(v: String) {
        _name.update { v }
    }

    private val _showAddBookmarkDialog = MutableStateFlow(false)
    val showAddBookmarkDialog = _showAddBookmarkDialog.asStateFlow()
    fun updateShowAddBookmarkDialog(v: Boolean) {
        _showAddBookmarkDialog.update { v }
    }

    fun initScreen(id: ObjectId, bookmarks: List<Bookmark>, groups: List<Group>) {

        val grp = groups.first { it._id == id }

        _group.update { grp }
        _name.update { grp.name }


        _groupBookmarks.update {
            bookmarks.filter { grp.bookmarks.contains(it._id) }
        }

        _screenInitialized.update { true }
    }

    fun deleteGroup(id: ObjectId, rootController: NavHostController) {
        viewModelScope.launch(Dispatchers.Main) {
            val queries = Queries(getRealm())
            queries.deleteGroup(id)

            rootController.openMain()
        }
    }

    fun updateGroup(id: ObjectId, rootController: NavHostController) {
        viewModelScope.launch(Dispatchers.Main) {
            val queries = Queries(getRealm())
            val bookmarksIds = groupBookmarks.value.map { it._id }

            queries.updateGroup(id, name.value, bookmarksIds)

            rootController.openMain()
        }
    }

    fun removeBookmark(bookmark: Bookmark) {

        _groupBookmarks.update {
            _groupBookmarks.value.filter { it._id != bookmark._id }
        }
    }

    fun addBookmark(bookmark: Bookmark){
        val newList = groupBookmarks.value.toMutableList()

        if(!newList.contains(bookmark)){
            newList.add(bookmark)
        }

        _groupBookmarks.update { newList }
        _showAddBookmarkDialog.update { false }
    }
}