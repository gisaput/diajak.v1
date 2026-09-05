package com.example.ui.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity

object NotificationHelper {
  private const val CHANNEL_ID = "diajak_notifications"
  private const val CHANNEL_NAME = "Aktivitas & Diskusi"
  private const val CHANNEL_DESC = "Notifikasi pemesanan, aktivitas baru, dan pesan dari kreator."

  fun showNotification(context: Context, title: String, message: String, type: String) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    // Create Notification Channel for API 26+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_HIGH
      ).apply {
        description = CHANNEL_DESC
      }
      notificationManager.createNotificationChannel(channel)
    }

    // Intent to open MainActivity when clicked
    val intent = Intent(context, MainActivity::class.java).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      putExtra("tab_to_open", when (type) {
        "booking" -> 2 // Bookings tab
        "message" -> 3 // Messages tab
        else -> 0 // Home/Explore tab
      })
    }

    val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
      PendingIntent.FLAG_UPDATE_CURRENT
    }

    val pendingIntent = PendingIntent.getActivity(
      context,
      System.currentTimeMillis().toInt(),
      intent,
      pendingIntentFlags
    )

    // Build the system notification
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.stat_notify_chat) // Standard safe system notification icon
      .setContentTitle(title)
      .setContentText(message)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)

    try {
      notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}
