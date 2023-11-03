package com.lighttigerxiv.bookmarks.frontend.screens.root.main.bookmarks

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.backend.utils.isAndroid13OrHigher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BookmarksScreenVM(application: Application) : AndroidViewModel(application) {

    private val context = application

    private val _screenInitialized = MutableStateFlow(false)
    val screenInitialized = _screenInitialized.asStateFlow()

    private var _currentBookmarks = MutableStateFlow<List<Bookmark>>(ArrayList())
    val currentBookmarks = _currentBookmarks.asStateFlow()
    fun updateCurrentBookmarks(v: List<Bookmark>) {
        _currentBookmarks.update { v }
    }

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()
    fun updateSearchText(v: String) {
        _searchText.update { v }
    }

    private val _showMenu = MutableStateFlow(false)
    val showMenu = _showMenu.asStateFlow()
    fun updateShowMenu(v: Boolean) {
        _showMenu.update { v }
    }


    fun initScreen(bookmarks: List<Bookmark>) {

        _currentBookmarks.update { bookmarks }
        _screenInitialized.update { true }
    }

    fun filterBookmarks(bookmarks: List<Bookmark>) {
        val newList = bookmarks.filter {
            it.name.trim().lowercase().contains(searchText.value.trim().lowercase())
        }

        _currentBookmarks.update { newList }
    }

    fun openUrl(url: String) {

        var validUrl = url

        if (!url.startsWith("https://") || !url.startsWith("http://")) {
            validUrl = "https://$url"
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun copyUrl(url: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Url", url)
        clipboardManager.setPrimaryClip(clip)

        if (!isAndroid13OrHigher()) {
            Toast.makeText(context, "Copied $url", Toast.LENGTH_LONG).show()
        }
    }
}