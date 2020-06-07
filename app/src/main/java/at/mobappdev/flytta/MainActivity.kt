package at.mobappdev.flytta

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import at.mobappdev.flytta.Exercise.Exercises
import at.mobappdev.flytta.history.History
import at.mobappdev.flytta.reminderlist.ReminderList

class MainActivity : AppCompatActivity() {
    private val channelName = "ReminderChannel"
    private val channelId = "1"
    private val channelDescription = "This is the ReminderChannel"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()
    }

    fun goToHistory(view: View) {
        val intent = Intent(this, History::class.java)
        startActivity(intent)
    }

    fun goToLogout(view: View) {
        val intent = Intent(this, Logout::class.java)
        startActivity(intent)
    }

    fun goToExercises(view: View) {
        val intent = Intent(this, Exercises::class.java)
        startActivity(intent)
    }

    fun goToReminderList(view: View) {
        val intent = Intent(this, ReminderList::class.java)
        startActivity(intent)
    }

    //creates notification channel for api26+
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = channelName
            val descriptionText = channelDescription
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            channel.enableVibration(true)

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
