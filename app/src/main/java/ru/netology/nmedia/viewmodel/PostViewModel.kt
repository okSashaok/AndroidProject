package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl

private val emptyPost = Post()

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    fun favoriteById(id: Long) = repository.favoriteById(id)
    fun shareById(id: Long) = repository.shareById(id)
    val edited = MutableLiveData(emptyPost)
    fun save(content: String) {
        edited.value?.let { post ->
            val trimmed = content.trim()
            if (trimmed != post.content) {
                repository.save(post.copy(content = content))
            }
            edited.value = emptyPost
        }
    }

    fun cancle() {
        edited.value = emptyPost
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun removeById(id: Long) = repository.removeById(id)
}