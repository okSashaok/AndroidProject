package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.concurrent.thread

private val emptyPost = Post()

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySQLiteImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(emptyPost)

    init {
        load()
    }

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    fun load() {
        thread {
            _data.postValue(FeedModel(loading = true))
            val state = try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: Exception) {
                FeedModel(errorCode = e.message.toString())
            }
            _data.postValue(state)
        }
    }

    fun favoriteById(id: Long) {
        thread {
            try {
                val updatePost = repository.favoriteById(id)
                _data.value?.posts?.let { posts ->
                    val newPosts = posts.map { post ->
                        if (post.id == id) updatePost else post
                    }
                    _data.postValue(FeedModel(posts = newPosts))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun shareById(id: Long) = repository.shareById(id)
    fun save(content: String) {
        thread {
            try {
                edited.value?.let { post ->
                    val trimmed = content.trim()
                    if (trimmed != post.content) {
                        repository.save(post.copy(content = content))
                    }
                    _postCreated.postValue(Unit)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            edited.postValue(emptyPost)
        }
    }

    fun cancel() {
        edited.value = emptyPost
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun removeById(id: Long) = repository.removeById(id)
}