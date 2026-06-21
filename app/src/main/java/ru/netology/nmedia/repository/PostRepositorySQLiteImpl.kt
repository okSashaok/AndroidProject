package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)
    private fun update() {
        data.value = posts
    }

    init {
        posts = dao.getAll()
        update()
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        update()
    }

    override fun favoriteById(id: Long) {
        dao.favoriteById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                favoriteByMe = !it.favoriteByMe,
                favorite = if (it.favoriteByMe) it.favorite - 1 else it.favorite + 1
            )
        }
        update()
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(
                share = it.share + 1
            )
        }
        update()
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter {
            it.id != id
        }
        update()
    }
}