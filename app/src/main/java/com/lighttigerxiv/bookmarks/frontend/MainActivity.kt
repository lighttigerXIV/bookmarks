package com.lighttigerxiv.bookmarks.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModelProvider
import com.lighttigerxiv.bookmarks.frontend.screens.root.RootScreen
import com.lighttigerxiv.bookmarks.frontend.system_bars.ChangeNavigationBarsColor
import com.lighttigerxiv.bookmarks.frontend.system_bars.ChangeStatusBarColor
import com.lighttigerxiv.bookmarks.frontend.ui.theme.BookmarksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = ViewModelProvider(this)[AppVM::class.java]
        val owner = this

        setContent {
            BookmarksTheme {

                ChangeStatusBarColor(MaterialTheme.colorScheme.surface)
                ChangeNavigationBarsColor(MaterialTheme.colorScheme.surface)

                RootScreen(appVM = vm, owner)
            }
        }
    }
}