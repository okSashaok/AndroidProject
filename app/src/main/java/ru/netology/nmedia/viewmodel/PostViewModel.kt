package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl
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
    fun load(){
        thread {
            _data.postValue(FeedModel(loading = true))
            val state = try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (_: Exception){
                FeedModel(error = true)
            }
            _data.postValue(state)
        }
    }
    fun favoriteById(id: Long) = repository.favoriteById(id)
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
            } catch (e: Exception){
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