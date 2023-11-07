package com.lighttigerxiv.bookmarks.backend.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    object Keys {
        val OPEN_GROUPS_BY_DEFAULT = booleanPreferencesKey("open_groups_by_default")
        val SEARCH_ON_OPEN = booleanPreferencesKey("search_on_open")
    }

    val settingsFlow: Flow<Settings> = dataStore.data.map { settings ->
        Settings(
            openGroupsByDefault = settings[Keys.OPEN_GROUPS_BY_DEFAULT] ?: false,
            searchOnOpen = settings[Keys.SEARCH_ON_OPEN] ?: false
        )
    }

    suspend fun updateOpenGroupsByDefault(v: Boolean) {
        dataStore.edit { it[Keys.OPEN_GROUPS_BY_DEFAULT] = v }
    }

    suspend fun updateSearchOnOpen(v: Boolean) {
        dataStore.edit { it[Keys.SEARCH_ON_OPEN] = v }
    }
}