package com.lighttigerxiv.bookmarks.frontend.screens.root.main.bookmarks.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.backend.realm.Queries
import com.lighttigerxiv.bookmarks.backend.realm.getRealm
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.navigation.openMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class BookmarkScreenVM() : ViewModel() {

    private var bookmark: Bookmark? = null

    private val _screenInitialized = MutableStateFlow(false)
    val dataInitiated = _screenInitialized.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog = _showDeleteDialog.asStateFlow()
    fun updateShowDeleteDialog(v: Boolean) {
        _showDeleteDialog.update { v }
    }

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    fun updateName(v: String) {
        _name.update { v }
    }

    private val _url = MutableStateFlow("")
    val url = _url.asStateFlow()
    fun updateUrl(v: String) {
        _url.update { v }
    }


    fun initScreen(id: ObjectId, bookmarks: List<Bookmark>) {
        viewModelScope.launch(Dispatchers.Main) {
            bookmark = bookmarks.find { it._id == id }

            bookmark?.let { bookmark ->
                _name.update { bookmark.name }
                _url.update { bookmark.url }

                _screenInitialized.update { true }
            }
        }
    }

    fun editBookmark(id: ObjectId, rootController: NavHostController, appVM: AppVM) {
        viewModelScope.launch(Dispatchers.Main) {
            val queries = Queries(getRealm())
            queries.updateBookmark(id, name.value, url.value)
            rootController.openMain()
        }
    }

    fun deleteBookmark(id: ObjectId, rootController: NavHostController){
        viewModelScope.launch(Dispatchers.Main){
            val queries = Queries(getRealm())
            queries.deleteBookmark(id)

            rootController.openMain()
        }
    }
}