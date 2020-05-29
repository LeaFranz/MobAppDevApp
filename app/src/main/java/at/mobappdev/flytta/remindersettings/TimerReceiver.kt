package at.mobappdev.flytta.remindersettings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import at.mobappdev.flytta.reminderlist.NotificationBuilder

class TimerReceiver : BroadcastReceiver() {

    //called on finished alarm
    override fun onReceive(context: Context, intent: Intent) {
        TimerUtil.setTimerState(ReminderSettings.TimerState.Stopped)
        TimerUtil.setAlarmSetTime(0)
        NotificationBuilder.sendNotification(context, "Get up and move!", "Tap here to get to your fun exercise :D")
    }

}
