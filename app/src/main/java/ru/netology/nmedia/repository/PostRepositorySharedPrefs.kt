package ru.netology.nmedia.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositoryFileImpl(private val context: Context) : PostRepository {
    private var nextID = 1L
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }
    private val data = MutableLiveData(posts)
    private fun update() {
        data.value = posts
    }

    init {
        val file = context.filesDir.resolve(FILENAME_POSTS)
        if(file.exists()) {
            context.openFileInput(FILENAME_POSTS).bufferedReader().use { str ->
                posts = gson.fromJson(str, typeToken)
                nextID = posts.maxOf { it.id } + 1
                update()
            }
        }
    }

    private fun sync() {
        context.openFileOutput(FILENAME_POSTS, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun favoriteById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                favoriteByMe = !it.favoriteByMe,
                favorite = if (it.favoriteByMe) it.favorite - 1 else it.favorite + 1
            )
        }
        update()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(share = it.share + 1)
        }
        update()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextID++,
                    author = "Me",
                    favorite = 0,
                    favoriteByMe = false,
                    datePublication = "Now"
                )
            ) + posts
        } else {
            posts = posts.map {
                if (it.id == post.id) {
                    it.copy(content = post.content)
                } else {
                    it
                }
            }
        }
        update()
    }

    override fun removeById(id: Long) {
        posts = posts.filterNot {
            it.id == id
        }
        update()
    }

    companion object {
        private const val FILENAME_POSTS = "posts.json"
        private val gson = Gson()
        private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }
}