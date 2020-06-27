package at.mobappdev.flytta.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import at.mobappdev.flytta.Exercise.Exercises
import at.mobappdev.flytta.R

class NotificationBuilder {

    companion object {
        /**
         * builds notification and sends it to device
         */
        fun sendNotification(context: Context, title:String, text:String){
            val intent = Intent(context, Exercises::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            //opens exercise screen on click
            //A PendingIntent is a token that you give to a foreign application (e.g. NotificationManager, AlarmManager, Home Screen AppWidgetManager, or other 3rd party applications), which allows the foreign application to use your application's permissions to execute a predefined piece of code.
           //https://stackoverflow.com/questions/2808796/what-is-an-android-pendingintent
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