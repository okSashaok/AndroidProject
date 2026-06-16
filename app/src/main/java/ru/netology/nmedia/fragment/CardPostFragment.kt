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
            binding.root.isClickable = false
            binding.author.text = post.author
            binding.datePublication.text = post.datePublication
            binding.content.text = post.content
            binding.buttonFavorite.isChecked = post.favoriteByMe
            binding.buttonFavorite.text = post.counterFormatting(post.favorite)
            binding.buttonShare.text = post.share.toString()
            binding.buttonVisibility.text = "5"
            if(post.video == null){
                binding.groupVideo.visibility = View.GONE
            }
            else{
                binding.groupVideo.visibility = View.VISIBLE
                binding.groupVideo.setOnClickListener {
                    val videoUri = post.video.toUri()
                    val intent = Intent(Intent.ACTION_VIEW, videoUri)
                    it.context.startActivity(intent)
                }
            }
            binding.buttonFavorite.setOnClickListener {
                viewModel.favoriteById(post.id)
            }
            binding.buttonShare.setOnClickListener {
                binding.buttonShare.isChecked = false
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
            }
            binding.buttonMenu.setOnClickListener {
                binding.buttonMenu.isChecked = true
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_post)
                    setOnDismissListener {
                        binding.buttonMenu.isChecked = false
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