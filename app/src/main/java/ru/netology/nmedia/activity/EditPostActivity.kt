package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityEditPostBinding
class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val initialText = intent.getStringExtra(Intent.EXTRA_TEXT)
        binding.editPost.setText(initialText)
        binding.editPost.requestFocus()
        binding.editPost.setSelection(binding.editPost.text?.length ?: 0)
        binding.bottonEdit.setOnClickListener {
            val content = binding.editPost.text?.toString()
            if (content.isNullOrBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                val resultIntent = Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, content)
                }
                setResult(RESULT_OK, resultIntent)
            }
            finish()
        }
    }
}
object EditPostContract : ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?): Intent =
        Intent(context, EditPostActivity::class.java).apply {
            putExtra(Intent.EXTRA_TEXT, input)
        }

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == AppCompatActivity.RESULT_OK) {
            intent?.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
}