package at.mobappdev.flytta.remindersettings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TimerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //TODO: show notification

        TimerUtil.setTimerState(ReminderSettings.TimerState.Stopped, context)
        TimerUtil.setAlarmSetTime(0, context)
    }
}
