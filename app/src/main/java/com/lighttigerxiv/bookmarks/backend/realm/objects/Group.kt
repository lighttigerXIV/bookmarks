package com.lighttigerxiv.bookmarks.backend.realm.objects

import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.ObjectId

class Group: RealmObject {
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var bookmarks: RealmList<ObjectId> = ArrayList<ObjectId>().toRealmList()
}