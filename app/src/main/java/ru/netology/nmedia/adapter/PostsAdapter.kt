package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

typealias Listener = (Post) -> Unit

class PostsAdapter(
    private val favoriteListener: Listener,
    private val shareListener: Listener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            CardPostBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return PostViewHolder(binding, favoriteListener, shareListener)
    }

    override fun onBindViewHolder(viewHolder: PostViewHolder, position: Int) {
        val post = getItem(position)
        viewHolder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val favoriteListener: Listener,
    private val shareListener: Listener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.author.text = post.author
        binding.datePublication.text = post.datePublication
        binding.content.text = post.content
        binding.favorite.text = post.counterFormatting(post.favorite)
        binding.imageFavorite.setImageResource(if (post.favoriteByMe) R.drawable.favorite_yes_24 else R.drawable.favorite_24)
        binding.share.text = post.counterFormatting(post.share)
        binding.imageFavorite.setOnClickListener {
            favoriteListener(post)
        }
        binding.imageShare.setOnClickListener {
            shareListener(post)
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}