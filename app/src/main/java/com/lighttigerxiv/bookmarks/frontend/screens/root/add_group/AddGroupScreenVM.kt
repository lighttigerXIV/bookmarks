package com.lighttigerxiv.bookmarks.frontend.screens.root.add_group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.backend.realm.Queries
import com.lighttigerxiv.bookmarks.backend.realm.getRealm
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.frontend.navigation.openMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class AddGroupScreenVM : ViewModel() {

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    fun updateName(v: String) {
        _name.update { v }
    }

    private var selectedBookmarks: List<ObjectId> = ArrayList()


    fun isBookmarkSelected(bookmark: Bookmark): Boolean {
        return selectedBookmarks.contains(bookmark._id)
    }

    fun toggleBookmark(bookmark: Bookmark) {

        val newList = selectedBookmarks.toMutableList()

        if (selectedBookmarks.contains(bookmark._id)) {
            newList.remove(bookmark._id)
        } else {
            newList.add(bookmark._id)
        }

        selectedBookmarks = newList
    }

    fun addGroup(rootController: NavHostController) {
        viewModelScope.launch(Dispatchers.Main) {

            val queries = Queries(getRealm())
            queries.addGroup(name.value, selectedBookmarks)

            rootController.openMain()
        }
    }
}