package ru.netology.nmedia.repository

import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl : PostRepository {

    override fun getAll(): List<Post> {
        val response = PostApi.service.getAll().execute()
        if(!response.isSuccessful){
            throw RuntimeException(response.code().toString())
        }
        return response.body().orEmpty()
    }

    override fun save(post: Post) {
        PostApi.service.save(post).execute()
    }

    override fun favoriteById(id: Long): Post {
        /*val getRequest = Request.Builder()
            .url("$BASE_URL/api/posts/$id")
            .get()
            .build()
        val getResponse = client.newCall(getRequest).execute()
        val postJson = getResponse.body.string()
        val currentPost = gson.fromJson(postJson, Post::class.java)
        val requestBuilder = Request.Builder().url("$BASE_URL/api/slow/posts/$id/likes")
        val request = if (currentPost.favoriteByMe) {
            requestBuilder.delete().build()
        } else {
            requestBuilder.post("".toRequestBody()).build()
        }
        val call = client.newCall(request)
        val response = call.execute()
        val responseText = response.body.string()
        return gson.fromJson(responseText, Post::class.java)*/
        return Post()
    }

    override fun shareById(id: Long) {
        //dao.shareById(id)
    }

    override fun removeById(id: Long) {
        PostApi.service.deleteById(id).execute()
    }
}