package com.lighttigerxiv.bookmarks.backend.realm

import com.lighttigerxiv.bookmarks.backend.realm.objects.Bookmark
import com.lighttigerxiv.bookmarks.backend.realm.objects.Group
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

fun getRealm(): Realm{
    val config = RealmConfiguration.Builder(
        schema = setOf(
            Bookmark::class,
            Group::class
        )
    )
        .schemaVersion(3)
        .compactOnLaunch()
        .build()

    return Realm.open(config)
}