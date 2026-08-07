package ru.netology.nmedia.dto

import com.google.gson.annotations.SerializedName
import kotlin.Long

data class Post(
    val id: Long = 0,
    val author: String = "",
    @SerializedName("published")
    val datePublication: Long = 0,//String = "",
    val content: String = "",
    @SerializedName("likes")
    val favorite: Int = 0,
    @SerializedName("likedByMe")
    val favoriteByMe: Boolean = false,
    /*val share: Int = 0,
    val video: String? = null*///"https://rutube.ru/video/6550a91e7e523f9503bed47e4c46d0cb"
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