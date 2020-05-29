package at.mobappdev.flytta.reminderlist

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import at.mobappdev.flytta.Exercise.Exercises
import at.mobappdev.flytta.R

class NotificationBuilder() {

    companion object {
        fun sendNotification(context: Context, title:String, text:String){
            //TODO: change intent class
            val intent = Intent(context, Exercises::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)


            val builder = NotificationCompat.Builder(context, "1")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_run)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                notify(1, builder.build())
            }
        }

    }

}