package com.lighttigerxiv.bookmarks.backend.utils

fun getFaviconUrl(url: String): String{
    return "https://www.google.com/s2/favicons?domain=${url}&sz=256"
}