package ru.netology.nmedia.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(inflater, container, false)
        AndroidUtils.showKeyboard(binding.newPostText)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        arguments?.textArg?.let {
            binding.newPostText.setText(it)
        }
        binding.bottonNew.setOnClickListener {
            val text = binding.newPostText.text.toString()
            if (!text.isBlank()) {
                viewModel.save(binding.newPostText.text.toString())
            }
            findNavController().navigateUp()
        }
        return binding.root
    }

    companion object {
        val KEY_POST_TEXT = "post_text"
        var Bundle.textArg by StringArg
    }
}

object NewPostContract : ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit) =
        Intent(context, NewPostFragment::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?) = intent?.getStringExtra(
        NewPostFragment.KEY_POST_TEXT
    )
}