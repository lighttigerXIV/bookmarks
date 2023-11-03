package com.lighttigerxiv.bookmarks.frontend

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.bookmarks.backend.realm.getRealm
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.backend.realm.Queries
import com.lighttigerxiv.bookmarks.backend.realm.objects.Group
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppVM(application: Application) : AndroidViewModel(application) {

    private val _initialized = MutableStateFlow(false)
    val initialized = _initialized.asStateFlow()

    private val realm = getRealm()
    private val queries = Queries(realm)

    private val _bookmarks = MutableStateFlow<List<Bookmark>>(ArrayList())
    val bookmarks = _bookmarks.asStateFlow()
    private fun refreshBookmarks(){
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                _bookmarks.update { queries.getBookmarks() }
            }
        }
    }

    private val _groups = MutableStateFlow<List<Group>>(ArrayList())
    val groups = _groups.asStateFlow()
    private fun refreshGroups(){
        viewModelScope.launch {
            withContext(Dispatchers.Main){
                _groups.update { queries.getGroups() }
            }
        }
    }



    init {
        viewModelScope.launch {
            withContext(Dispatchers.Main){

                _bookmarks.update { queries.getBookmarks() }
                _groups.update { queries.getGroups() }

                _initialized.update { true }

                val realm = getRealm()
                val bookmarksFlow = realm.query(Bookmark::class).asFlow()
                val groupsFlow = realm.query(Group::class).asFlow()

                bookmarksFlow.collect{
                    if(it is UpdatedResults){
                        refreshBookmarks()
                    }
                }

                groupsFlow.collect{
                    if(it is UpdatedResults){
                        refreshGroups()
                    }
                }
            }
        }
    }
}