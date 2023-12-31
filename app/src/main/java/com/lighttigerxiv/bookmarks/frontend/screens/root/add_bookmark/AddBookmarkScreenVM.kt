package com.lighttigerxiv.bookmarks.frontend.screens.root.add_bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lighttigerxiv.bookmarks.backend.realm.Queries
import com.lighttigerxiv.bookmarks.backend.realm.getRealm
import com.lighttigerxiv.bookmarks.frontend.navigation.openMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddBookmarkScreenVM : ViewModel() {

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

    fun isSaveButtonDisabled(): Boolean {
        return name.value.isEmpty() || url.value.isEmpty()
    }

    fun addBookmark(rootController: NavHostController) {
        viewModelScope.launch(Dispatchers.Main) {

            rootController.openMain()

            val realm = Queries(getRealm())
            realm.addBookmark(
                name = name.value,
                url = url.value
            )
        }
    }
}