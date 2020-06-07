package at.mobappdev.flytta.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import at.mobappdev.flytta.remindersettings.ReminderPrefs
import java.text.SimpleDateFormat
import java.util.*

class IntervalAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("IntervalAlarmReceiver", "Interval Alarm received")
        val date = Date()
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.GERMAN)
        val currentTime = timeFormatter.format(date)

        if (currentTime > ReminderPrefs.getCurrentEndTime(context).toString()) {
            Alarms.removeIntervalAlarm(context)
            return
        }

        NotificationBuilder.sendNotification(
            context,
            "Get up and move!",
            "Tap here to go to your fun exercise :D"
        )

    }
}