package ru.netology.nmedia.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "author")
    val author: String = "",
    @ColumnInfo(name = "datePublication")
    val datePublication: String = "",
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "favorite")
    val favorite: Int = 0,
    @ColumnInfo(name = "favoriteByMe")
    val favoriteByMe: Boolean = false,
    @ColumnInfo(name = "share")
    val share: Int = 0,
    @ColumnInfo(name = "video")
    val video: String? = null
) {
    fun toDto(): Post =
        Post(id, author, datePublication, content, favorite, favoriteByMe, share, video)

    companion object {
        fun fromDto(dto: Post): PostEntity = with(dto) {
            PostEntity(id, author, datePublication, content, favorite, favoriteByMe, share, video)
        }
    }
}