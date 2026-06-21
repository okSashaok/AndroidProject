package ru.netology.nmedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostContract
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class CardPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCardPostBinding.inflate(inflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val editPostContact = registerForActivityResult(EditPostContract) { result ->
            result ?: return@registerForActivityResult
            viewModel.save(result)
        }
        val postID = arguments?.getLong("postID")?: return binding.root
        viewModel.data.observe(viewLifecycleOwner){posts->
            val post = posts.find{
                it.id == postID
            }
            if(post == null){
                findNavController().navigateUp()
                return@observe
            }
            binding.cardPost.root.isClickable = false
            binding.cardPost.author.text = post.author
            binding.cardPost.datePublication.text = post.datePublication
            binding.cardPost.content.text = post.content
            binding.cardPost.buttonFavorite.isChecked = post.favoriteByMe
            binding.cardPost.buttonFavorite.text = post.counterFormatting(post.favorite)
            binding.cardPost.buttonShare.text = post.share.toString()
            binding.cardPost.buttonVisibility.text = "5"
            if(post.video == null){
                binding.cardPost.groupVideo.visibility = View.GONE
            }
            else{
                binding.cardPost.groupVideo.visibility = View.VISIBLE
                binding.cardPost.groupVideo.setOnClickListener {
                    val videoUri = post.video.toUri()
                    val intent = Intent(Intent.ACTION_VIEW, videoUri)
                    it.context.startActivity(intent)
                }
            }
            binding.cardPost.buttonFavorite.setOnClickListener {
                viewModel.favoriteById(post.id)
            }
            binding.cardPost.buttonShare.setOnClickListener {
                binding.cardPost.buttonShare.isChecked = false
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
            }
            binding.cardPost.buttonMenu.setOnClickListener {
                binding.cardPost.buttonMenu.isChecked = true
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnDismissListener {
                        binding.cardPost.buttonMenu.isChecked = false
                    }
                    setOnMenuItemClickListener { item->
                        when(item.itemId){
                            R.id.remove -> {
                                viewModel.removeById(post.id)
                                findNavController().navigateUp()
                                true
                            }
                            R.id.edit -> {
                                viewModel.edit(post)
                                editPostContact.launch(post.content)
                                true
                            }
                            else -> false
                        }
                    }
                    show()
                }
            }
        }
        return binding.root
    }
}