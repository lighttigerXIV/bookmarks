package com.lighttigerxiv.bookmarks.frontend.navigation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.lighttigerxiv.bookmarks.frontend.screens.root.add_bookmark.AddBookmarkScreenVM
import org.mongodb.kbson.ObjectId

fun goToNavbarRoute(controller: NavController, route: String){
    controller.navigate(route){
        popUpTo(controller.graph.findStartDestination().id){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavController.goBack(){
    this.navigateUp()
}

fun NavController.openMain(){
    this.navigate(Routes.Main){
        launchSingleTop = true
        restoreState = true
        popBackStack()
    }
}

fun NavController.openBookmarks(){
    goToNavbarRoute(this, Routes.Bookmarks)
}

fun NavController.openGroups(){
    goToNavbarRoute(this, Routes.Groups)
}

fun NavController.openAddBookmark(){
    this.navigate(Routes.AddBookmark)
}

fun NavController.openAddGroup(){
    this.navigate(Routes.AddGroup)
}

fun NavController.openBookmark(id: ObjectId){
    this.navigate("${Routes.Bookmark}/${id.toHexString()}")
}