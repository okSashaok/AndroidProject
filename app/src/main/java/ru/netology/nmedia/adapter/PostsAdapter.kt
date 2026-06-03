package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface PostListener {
    fun favorite(post: Post)
    fun share(post: Post)
    fun edit(post: Post)
    fun remove(post: Post)
}

class PostsAdapter(
    private val listener: PostListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            CardPostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return PostViewHolder(binding, listener)
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val post = getItem(position)
        viewHolder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val listener: PostListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.author.text = post.author
        binding.datePublication.text = post.datePublication
        binding.content.text = post.content
        //binding.favorite.text = post.counterFormatting(post.favorite)
        //binding.imageFavorite.setImageResource(if (post.favoriteByMe) R.drawable.favorite_yes_24 else R.drawable.favorite_24)
        binding.imageFavorite.isChecked = post.favoriteByMe
        binding.imageFavorite.text = post.counterFormatting(post.favorite).toString()
        binding.share.text = post.counterFormatting(post.share)
        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.menu_post)
                setOnMenuItemClickListener { item ->
                    Unit
                    when (item.itemId) {
                        R.id.remove -> {
                            listener.remove(post)
                            true
                        }

                        R.id.edit -> {
                            listener.edit(post)
                            true
                        }

                        else -> false
                    }
                }
                show()
            }
        }
        binding.imageFavorite.setOnClickListener {
            listener.favorite(post)
        }
        binding.imageShare.setOnClickListener {
            listener.share(post)
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}