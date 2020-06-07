package at.mobappdev.flytta.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import at.mobappdev.flytta.reminderlist.ReminderListItem
import at.mobappdev.flytta.remindersettings.ReminderPrefs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Alarms {
    companion object {
        fun setDailyAlarm(context: Context, item: ReminderListItem) {
            Log.i("Alarms", "Daily Alarm started: " + item)
            ReminderPrefs.setCurrentEndTime(item.timeEnd, context)
            val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val startHour = item.timeStart.split(":")[0].toInt()
            val startMinutes = item.timeStart.split(":")[1].toInt()
            val intent = Intent(
                context,
                DailyAlarmReceiver::class.java
            )

            //for starting on reboot
            intent.putExtra("startTime", item.timeStart)
            intent.putExtra("interval", item.interval)
            intent.putExtra("endTime", item.timeEnd)
            intent.putExtra("days", ArrayList(item.days))

            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()

                set(Calendar.HOUR_OF_DAY, startHour)
                set(Calendar.MINUTE, startMinutes)
            }

            alarmManger.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                1000 * (24 * 60 * 60) * 1,
                pendingIntent
            )
        }


        fun setIntervalAlarm(
            context: Context,
            start: String,
            end: String,
            alarmInterval: String,
            days: MutableList<*>
        ) {
            val interval = alarmInterval.toLong()
            val startHour = start.split(":")[0].toInt()
            val startMinutes = start.split(":")[1].toInt()
            val date = Date()
            val timeFormatter = SimpleDateFormat("HH:mm", Locale.GERMAN)
            val weekdayFormatter = SimpleDateFormat("EEEE", Locale.UK)
            val currentTime = timeFormatter.format(date)
            val dayOfWeek = weekdayFormatter.format(date)
            val dayInList =
                days.find { day -> dayOfWeek.toUpperCase(Locale.ROOT) == day }

            val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(
                context,
                IntervalAlarmReceiver::class.java
            )
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, startHour)
                set(Calendar.MINUTE, startMinutes)
            }

            if (currentTime > end && dayInList != null) {
                Log.i("Alarms", "Interval Reminder startet")
                alarmManger.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    1000 * (interval * 60) * 1,
                    pendingIntent
                )
            }
        }

        fun removeDailyAlarm(context: Context) {
            val intent = Intent(context, DailyAlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManger.cancel(pendingIntent)
        }

        fun removeIntervalAlarm(context: Context) {
            val intent = Intent(context, IntervalAlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManger.cancel(pendingIntent)
        }
    }
}