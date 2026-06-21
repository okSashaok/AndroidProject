package ru.netology.nmedia.dto

import kotlin.Long

data class Post(
    val id: Long = 0,
    val author: String = "",
    val datePublication: String = "",
    val content: String = "",
    val favorite: Int = 0,
    val favoriteByMe: Boolean = false,
    val share: Int = 0,
    val video: String? = null//"https://rutube.ru/video/6550a91e7e523f9503bed47e4c46d0cb"
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