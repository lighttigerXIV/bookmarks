package com.lighttigerxiv.bookmarks.frontend.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lighttigerxiv.bookmarks.frontend.AppVM
import com.lighttigerxiv.bookmarks.frontend.navigation.Routes
import com.lighttigerxiv.bookmarks.frontend.screens.root.add_bookmark.AddBookmarkScreen
import com.lighttigerxiv.bookmarks.frontend.screens.root.add_group.AddGroupScreen
import com.lighttigerxiv.bookmarks.frontend.screens.root.main.MainScreen
import com.lighttigerxiv.bookmarks.frontend.screens.root.main.MainScreenVM
import com.lighttigerxiv.bookmarks.frontend.screens.root.main.bookmarks.bookmark.BookmarkScreen
import org.mongodb.kbson.ObjectId

@Composable
fun RootScreen(
    appVM: AppVM,
    owner: ViewModelStoreOwner
) {

    val rootController = rememberNavController()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        NavHost(
            navController = rootController,
            startDestination = Routes.Main
        ) {
            composable(Routes.Main) {
                MainScreen(
                    rootController = rootController
                )
            }

            composable(Routes.AddBookmark) {

                AddBookmarkScreen(rootController)
            }

            composable(Routes.AddGroup) {

                AddGroupScreen(rootController)
            }

            composable(
                "${Routes.Bookmark}/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType }
                )
            ) {

                BookmarkScreen(
                    id = ObjectId(it.arguments!!.getString("id")!!),
                    rootController = rootController
                )
            }
        }
    }
}