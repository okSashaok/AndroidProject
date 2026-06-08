package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
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
        val postContract = registerForActivityResult(NewPostContract){
            result -> Unit
            result?: return@registerForActivityResult
            viewModel.save(result)
        }
        val groupInput = findViewById<Group>(R.id.groupInput)
        groupInput.visibility = View.GONE
        val adapter = PostsAdapter(
            object : PostListener {
                override fun favorite(post: Post) {
                    viewModel.favoriteById(post.id)
                }

                override fun share(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    }
                    val chooser = Intent.createChooser(intent, post.content)
                    startActivity(chooser)
                    //viewModel.shareById(post.id)
                }

                override fun edit(post: Post) {
                    groupInput.visibility = View.VISIBLE
                    viewModel.edit(post)
                }
                override fun remove(post: Post) {
                    viewModel.removeById(post.id)
                }
            }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this){
            posts-> Unit
            adapter.submitList(posts)
        }
        binding.add.setOnClickListener{
            postContract.launch()
        }
    }
}