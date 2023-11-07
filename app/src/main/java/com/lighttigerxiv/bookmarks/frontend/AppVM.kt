package com.lighttigerxiv.bookmarks.frontend

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lighttigerxiv.bookmarks.backend.realm.Queries
import com.lighttigerxiv.bookmarks.backend.realm.getRealm
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.backend.realm.objects.Group
import com.lighttigerxiv.bookmarks.backend.settings.Settings
import com.lighttigerxiv.bookmarks.backend.settings.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppVM(application: Application) : AndroidViewModel(application) {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    private val context = application

    private val _initialized = MutableStateFlow(false)
    val initialized = _initialized.asStateFlow()

    private val realm = getRealm()
    private val queries = Queries(realm)

    private val _bookmarks = MutableStateFlow<List<Bookmark>>(ArrayList())
    val bookmarks = _bookmarks.asStateFlow()
    private fun refreshBookmarks() {
        viewModelScope.launch(Dispatchers.Main) {
            _bookmarks.update { queries.getBookmarks() }
        }
    }

    private val _groups = MutableStateFlow<List<Group>>(ArrayList())
    val groups = _groups.asStateFlow()
    private fun refreshGroups() {
        viewModelScope.launch(Dispatchers.Main) {
            _groups.update { queries.getGroups() }
        }
    }

    private val _settings = MutableStateFlow<Settings?>(null)
    val settings = _settings.asStateFlow()

    fun updateOpenGroupsByDefault(v: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            SettingsRepository(context.dataStore).updateOpenGroupsByDefault(v)
        }
    }

    fun updateSearchOnOpen(v: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            SettingsRepository(context.dataStore).updateSearchOnOpen(v)
        }
    }

    private val _requestedFocusSearch = MutableStateFlow(false)
    val requestedFocusSearch = _requestedFocusSearch.asStateFlow()
    fun updateRequestedFocusSearch(v: Boolean){
        _requestedFocusSearch.update{ v }
    }


    init {
        viewModelScope.launch(Dispatchers.Main) {

            _bookmarks.update { queries.getBookmarks() }
            _groups.update { queries.getGroups() }


            SettingsRepository(application.dataStore).settingsFlow.collect { settings ->
                _settings.update { settings }
                _initialized.update { true }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            getRealm().query(Bookmark::class).asFlow().collect {
                refreshBookmarks()
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            getRealm().query(Group::class).asFlow().collect {
                refreshGroups()
            }
        }
    }
}