package com.tokland.sensors

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {

    // Called when the service is first created
    override fun onCreate() {
        super.onCreate()
        // Create a notification channel for Android 8.0 and above
        createNotificationChannel()
    }

    // Called when the service is started
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create a notification and start the service in the foreground
        val notification = createNotification()
        startForeground(1, notification)

        // Do any background tasks you need here
        println("tokland:debug:onStartCommand service")

        // Return START_NOT_STICKY if the system should not restart your service if it's killed
        return START_NOT_STICKY
    }

    // Create a notification channel, required for Android 8.0 and above
    private fun createNotificationChannel() {
        // Only create a channel on devices running Android 8.0 or above
            val channelId = "YOUR_CHANNEL_ID"  // Channel ID must be unique within your app
            val channelName = "Background Service Channel"  // Visible name of the channel
            val importance = NotificationManager.IMPORTANCE_LOW  // Choose the importance of the notification
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = "Channel for background service notifications"

            // Register the channel with the system
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
    }

    // Create the notification that will appear in the status bar
    private fun createNotification(): Notification {
        // Intent that opens the app when the notification is clicked
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)

        // Build the notification
        return NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
            .setContentTitle("App is running in the background")  // Title of the notification
            .setContentText("Your app is performing work in the background")  // Notification message
            .setSmallIcon(R.drawable.ic_launcher_foreground)  // Set an icon for the notification
            .setContentIntent(pendingIntent)  // Set what happens when the notification is clicked
            .build()  // Build the notification object
    }

    // Stop the foreground service when no longer needed
    /*
    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)  // Stop the service and remove the notification
    }
     */

    // Return null since this service does not allow binding
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /*
    private fun createNotificationChannel2() {
        // Notification channels are only required for Android 8.0 (API level 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "YOUR_CHANNEL_ID" // This can be any unique string ID for your channel
            val channelName = "Background Service Channel"
            val importance = NotificationManager.IMPORTANCE_LOW // This controls the level of detail of notifications
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = "Channel for background service notifications"

            // Register the channel with the system
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
     */

}

