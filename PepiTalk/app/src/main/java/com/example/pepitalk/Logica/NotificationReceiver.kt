package com.example.pepitalk.Logica

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.pepitalk.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "language_learning_channel"
            val channelName = "Notificaciones importantes"
            val channelDescription = "Canal para notificaciones emergentes"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val randomTextsLarge = listOf(
            "¡Deberías aprender algo de italiano! ¿Qué tal empezar con unas frases básicas?",
            "¿Por qué no intentas aprender alemán en un nuevo grupo? ¡Es un idioma muy interesante!",
            "Hay nuevos grupos de francés disponibles. ¡No pierdas la oportunidad de aprender un idioma tan bonito!",
            "Nuevo grupo de inglés para principiantes. ¡Es el momento perfecto para empezar a aprender!"
        )
        val randomText = randomTextsLarge.random()

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pepitoempresario)

        val builder = NotificationCompat.Builder(context, "language_learning_channel")
            .setSmallIcon(R.drawable.pepitalk)
            .setContentTitle("Pepito dice...")
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigTextStyle().bigText(randomText)).setColor(ContextCompat.getColor(context, R.color.morado))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        with(NotificationManagerCompat.from(context)) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(101, builder.build())
            }
        }
    }
}