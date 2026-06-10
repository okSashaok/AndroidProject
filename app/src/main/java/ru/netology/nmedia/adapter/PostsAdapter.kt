package ru.netology.nmedia.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.net.toUri
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
        if (post.video == null) {
            binding.groupVideo.visibility = View.GONE
        } else {
            binding.groupVideo.visibility = View.VISIBLE
            binding.groupVideo.setOnClickListener {
                val videoUri = post.video.toUri()
                val intent = Intent(Intent.ACTION_VIEW, videoUri)
                it.context.startActivity(intent)
            }
        }
        binding.buttonFavorite.isChecked = post.favoriteByMe
        binding.buttonFavorite.text = post.counterFormatting(post.favorite)
        binding.buttonFavorite.setOnClickListener {
            listener.favorite(post)
        }
        binding.buttonShare.text = post.share.toString()
        binding.buttonShare.setOnClickListener {
            binding.buttonShare.isChecked = false
            listener.share(post)
        }
        binding.buttonVisibility.text = 5.toString()
        binding.buttonMenu.setOnClickListener {
            binding.buttonMenu.isChecked = true
            PopupMenu(it.context, it).apply {
                inflate(R.menu.menu_post)
                setOnDismissListener {
                    binding.buttonMenu.isChecked = false
                }
                setOnMenuItemClickListener { item ->
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
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}