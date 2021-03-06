package at.mobappdev.flytta.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log

class DailyAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("DailyAlarmReceiver", "Daily Alarm received")

        //sets interval alarm with intent-params every day
        Alarms.setIntervalAlarm(context, intent.getStringExtra("startTime"), intent.getStringExtra("endTime"), intent.getStringExtra("interval"), intent.getParcelableArrayListExtra<Parcelable>("days"))
    }

}
