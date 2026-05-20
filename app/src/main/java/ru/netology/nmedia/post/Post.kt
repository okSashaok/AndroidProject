package ru.netology.nmedia.post

import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import kotlin.Long

class Post {
    private val id: Long
    private val author: String
    private val datePublication: String
    private val content: String
    private var favorite: Int
    private var favoriteByMe: Boolean
    private var share: Int
    private fun counterFormatting(counter: Int): String {
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
    public constructor(
        setId: Long,
        setAuthor: String,
        setDatePublication: String,
        setContent: String,
        setFavorite: Int = 0,
        setFavoriteByMe: Boolean = false,
        setShare: Int = 0
    ) {
        id = setId
        author = setAuthor
        datePublication = setDatePublication
        content = setContent
        favorite = setFavorite
        favoriteByMe = setFavoriteByMe
        share = setShare
    }
    public fun installation(binding: ActivityMainBinding) {
        binding.author.text = author
        binding.datePublication.text = datePublication
        binding.content.text = content
        binding.favorite.text = counterFormatting(favorite)
        binding.imageFavorite.setOnClickListener {
            if (favoriteByMe) {
                --favorite
                binding.imageFavorite.setImageResource(R.drawable.favorite_24)
            } else {
                ++favorite
                binding.imageFavorite.setImageResource(R.drawable.favorite_yes_24)
            }
            favoriteByMe = !favoriteByMe
            binding.favorite.text = counterFormatting(favorite)
        }
        binding.share.text = counterFormatting(share)
        binding.imageShare.setOnClickListener {
            ++share
            binding.share.text = counterFormatting(share)
        }
    }
}