package at.mobappdev.flytta.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import at.mobappdev.flytta.remindersettings.ReminderPrefs
import java.text.SimpleDateFormat
import java.util.*

//A broadcast receiver (receiver) is an Android component which allows you to register for system or application events. All registered receivers for an event are notified by the Android runtime once this event happens.
//gets called for interval alarm
class IntervalAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("IntervalAlarmReceiver", "Interval Alarm received")
        val date = Date()
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.GERMAN)
        val currentTime = timeFormatter.format(date)

        //stops the interval-reminder if the current time is after the specified end-time of the daily alarm
        if (currentTime > ReminderPrefs.getCurrentEndTime(context).toString()) {
            Alarms.removeIntervalAlarm(context)
            return
        }

        //sends notification via NotificationBuilder
        NotificationBuilder.sendNotification(
            context,
            "Get up and move!",
            "Tap here to go to your fun exercise :D"
        )

    }
}