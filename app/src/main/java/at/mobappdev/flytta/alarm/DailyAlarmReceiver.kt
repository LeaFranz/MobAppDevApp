package at.mobappdev.flytta.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import at.mobappdev.flytta.alarm.Alarms
import java.util.*

class DailyAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("DailyAlarmReceiver", "Daily Alarm received")

        Alarms.setIntervalAlarm(context, intent.getStringExtra("startTime"), intent.getStringExtra("endTime"), intent.getStringExtra("interval"), intent.getParcelableArrayListExtra<Parcelable>("days"))
    }

}
