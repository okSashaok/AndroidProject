package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val left = systemBars.left + v.paddingLeft
            val top = systemBars.top// + v.paddingTop
            val right = systemBars.right + v.paddingRight
            val bottom = systemBars.bottom
            v.setPadding(left, top, right, bottom)
            insets
        }
        val viewModel by viewModels<PostViewModel>()
        viewModel.data.observe(this){
            post-> Unit
            binding.author.text = post.author
            binding.datePublication.text = post.datePublication
            binding.content.text = post.content
            binding.favorite.text = post.counterFormatting(post.favorite)
            binding.imageFavorite.setOnClickListener {
                viewModel.favorite()
            }
            binding.imageFavorite.setImageResource(if(post.favoriteByMe) R.drawable.favorite_yes_24 else R.drawable.favorite_24)
            binding.imageShare.setOnClickListener {
                viewModel.share()
            }
            binding.share.text = post.counterFormatting(post.share)
        }
    }
}