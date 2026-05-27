package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding
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
        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter({
            viewModel.favoriteById(it.id)
        })
        binding.main.adapter = adapter
        viewModel.data.observe(this){
            posts-> Unit
            //adapter.list = posts
            adapter.submitList(posts)
        }
    }
}