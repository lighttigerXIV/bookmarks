package com.lighttigerxiv.bookmarks.backend.realm.objects

import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.ObjectId

class Bookmark: RealmObject {
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var url: String = ""
}