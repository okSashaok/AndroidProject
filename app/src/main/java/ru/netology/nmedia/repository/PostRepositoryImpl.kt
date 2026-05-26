package ru.netology.nmedia.repository

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl : PostRepository {
    private var post = Post(
        1,
        "Нетология. Университет интернет-профессия будущего",
        "21 мая в 18:36",
        "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия – помочь встать на путь роста и начать цепочку перемен › http://netolo.gy/fyb",
        999,
        false,
        990
    )
    private val data = MutableLiveData(post)
    private fun update(){
        data.value = post
    }
    override fun get() = data
    override fun favorite() {
        post = post.copy(
            favoriteByMe = !post.favoriteByMe,
            favorite = if(post.favoriteByMe) post.favorite - 1 else post.favorite + 1
        )
        update()
    }
    override fun share(){
        post = post.copy(share = post.share + 1)
        update()
    }
}