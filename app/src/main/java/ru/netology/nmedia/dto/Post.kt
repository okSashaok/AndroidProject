package ru.netology.nmedia.dto

import kotlin.Long

data class Post(
    var id: Long,
    var author: String,
    var datePublication: String,
    var content: String,
    var favorite: Int,
    var favoriteByMe: Boolean,
    var share: Int
){
    public fun counterFormatting(counter: Int): String {
        if (counter < 1_000) {
            return counter.toString()
        }
        var copyFavorite = counter
        if (counter < 10_000) {
            copyFavorite /= 1_00
            return (copyFavorite.toFloat() / 10).toString() + "K"
        }
        if (counter < 1_000_000) {
            return (counter / 1_000).toString() + "K"
        }
        copyFavorite /= 100_000
        return (copyFavorite.toDouble() / 10).toString() + "M"
    }
}