package ru.netology.nmedia.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    private companion object {
        const val channelId = "default"
        const val ACTION_KEY = "action"
        const val CONTENT_KEY = "content"
        val gson = Gson()
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(token: String) {
        println(token)
    }

    private fun messageProcessingError(print: String){
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Processing error")
            .setContentText(print)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        notify(notification)
    }

    private fun handleNewPost(content: NewPost){
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_new_post,
                    content.userName
                )
            )
            .setStyle(NotificationCompat.BigTextStyle().bigText(content.postContent))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        notify(notification)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val actionStr = message.data[ACTION_KEY]
        val action = Action.values().find {
            it.name == actionStr
        }
        when (action) {
            Action.FAVORITE -> handleFavorite(gson.fromJson(message.data[CONTENT_KEY], Favorite::class.java))
            Action.NEW_POST -> handleNewPost(gson.fromJson(message.data[CONTENT_KEY], NewPost::class.java))
            null -> messageProcessingError(actionStr.toString())
        }
    }

    private fun notify(notification: Notification) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ){
            NotificationManagerCompat.from(this).notify(Random.nextInt(100_000), notification)
        }
    }

    private fun handleFavorite(content: Favorite) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_favorite,
                    content.userName,
                    content.postAuthor
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        notify(notification)
    }
}

enum class Action {
    FAVORITE,
    NEW_POST
}

data class Favorite(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String
)
data class NewPost(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postContent: String
)