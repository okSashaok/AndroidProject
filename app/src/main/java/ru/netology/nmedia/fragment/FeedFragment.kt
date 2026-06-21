package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostContract
import ru.netology.nmedia.adapter.PostListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstaneState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val editPostContact = registerForActivityResult(EditPostContract) { result ->
            result ?: return@registerForActivityResult
            viewModel.save(result)
        }
        val adapter = PostsAdapter(
            object : PostListener {
                override fun favorite(post: Post) {
                    viewModel.favoriteById(post.id)
                }

                override fun share(post: Post) {
                    viewModel.shareById(post.id)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    }
                    val chooser =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(chooser)
                }

                override fun edit(post: Post) {
                    viewModel.edit(post)
                    editPostContact.launch(post.content)
                }

                override fun remove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun focusOnThePost(post: Post) {
                    findNavController().navigate(
                        R.id.action_feedFragment_to_postCardFragment,
                        Bundle().apply {
                            putLong("postID", post.id)
                        }
                    )
                }
            }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }
}