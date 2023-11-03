package com.lighttigerxiv.bookmarks.backend.realm

import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.backend.realm.objects.Group
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import org.mongodb.kbson.ObjectId

class Queries(private val realm: Realm) {

    fun getBookmarks(): List<Bookmark> {
        val bookmarks = realm.query<Bookmark>().find().toMutableList()
        bookmarks.sortBy { it.name }

        return bookmarks
    }

    suspend fun addBookmark(name: String, url: String) {

        val bookmark = Bookmark()
        bookmark.name = name
        bookmark.url = url

        realm.write {
            copyToRealm(bookmark)
        }
    }

    suspend fun updateBookmark(id: ObjectId, name: String, url: String, onFinish: () -> Unit = {}) {
        realm.write {
            val bookmark: Bookmark? = this.query<Bookmark>("_id == $0", id).first().find()
            bookmark?.name = name
            bookmark?.url = url
            onFinish()
        }

        realm.close()
    }

    fun getGroups(): List<Group> {
        val groups = realm.query<Group>().find().toMutableList()
        groups.sortBy { it.name }

        return groups
    }

    suspend fun addGroup(name: String, bookmarks: List<ObjectId>) {
        val group = Group()
        group.name = name
        group.bookmarks = bookmarks.toRealmList()

        realm.write {
            copyToRealm(group)
        }

        realm.close()
    }
}