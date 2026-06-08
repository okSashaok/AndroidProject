package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.ok.setOnClickListener{
            val text = binding.edit.text.toString()
            if(text.isBlank()){
                setResult(RESULT_CANCELED)
            }
            else{
                setResult(RESULT_OK, Intent().apply {
                    putExtra()
                })
            }
        }
    }
    companion object{
        val KEY_POST_TEXT = "post_text"
    }
}
object NewPostContract: ActivityResultContracts<Unit, String?>(){
    override fun createIntent(context: Context, input: Intent) = Intent(context, NewPostActivity::class.java)
    override fun parseResult(resultCode: Int, intent: Intent?) = intent?.getStringExtra(
        NewPostActivity.KEY_POST_TEXT)
}