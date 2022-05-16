package ru.samitin.weather.experement

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.samitin.weather.R

class MyFirebaseMassagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteMassageData = remoteMessage.data
        if (remoteMassageData.isNotEmpty()){
            handleDataMassage(remoteMassageData.toMap())
        }
    }

    private fun handleDataMassage(data: Map<String, String>) {
        val title = data[PUSH_KEY_TITLE]
        val massage = data[PUSH_KEY_MESSAGE]
        if (!title.isNullOrBlank() && !massage.isNullOrBlank())
            showNotification(title,massage)
    }

    private fun showNotification(title: String, massage: String) {
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_earth)
            setContentTitle(title)
            setContentText(massage)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }
        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "Channel name"
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {

    }

    companion object {
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 37
    }
}