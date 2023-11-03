package com.lighttigerxiv.bookmarks.frontend.screens.root.add_bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.bookmarks.backend.realm.Queries
import com.lighttigerxiv.bookmarks.backend.realm.getRealm
import com.lighttigerxiv.bookmarks.frontend.AppVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun isSaveButtonDisabled(): Boolean{
        return name.value.isEmpty() || url.value.isEmpty()
    }

    fun addBookmark() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val realm = Queries(getRealm())
                realm.addBookmark(
                    name = name.value,
                    url = url.value
                )
            }
        }
    }

    fun resetScreen(){
        _name.update { "" }
        _url.update { "" }
    }
}