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
        var currentReminderId = ""

        /**
         * sets alarm that gets called daily using AlarmManager
         */
        fun setDailyAlarm(context: Context, item: ReminderListItem) {
            Log.i("Alarms", "Daily Alarm started: $item")
            currentReminderId = item.reminderID
            ReminderPrefs.setCurrentEndTime(item.timeEnd, context)
            val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val startHour = item.timeStart.split(":")[0].toInt()
            val startMinutes = item.timeStart.split(":")[1].toInt()
            val intent = Intent(
                context,
                DailyAlarmReceiver::class.java
            )

            //starting on reboot only worked with giving the params to the receiver via the intent
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
                calendar.timeInMillis, //start time
                1000 * (24 * 60 * 60) * 1, //every day
                pendingIntent
            )
        }


        /**
         * sets alarm using the specified interval
         * gets called every day from DailyAlarmReceiver
         * params: from intent from dailyAlarm
         */
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
            //check if current day is in list
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

            //reminder starts if the day is in the list and the time frame is not over for the day
            if (currentTime < end && dayInList != null) {
                Log.i("Alarms", "Interval Reminder started")
                alarmManger.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis, //star time
                    1000 * (interval * 60) * 1, //interval
                    pendingIntent
                )
            }
        }

        /**
         * cancels daily alarm
         */
        fun removeDailyAlarm(context: Context) {
            Log.i("Alarms", "Daily Reminder removed")
            currentReminderId = ""
            val intent = Intent(context, DailyAlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManger.cancel(pendingIntent)
        }

        /**
         * cancels interval alarm
         */
        fun removeIntervalAlarm(context: Context) {
            Log.i("Alarms", "Interval Reminder removed")
            val intent = Intent(context, IntervalAlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManger.cancel(pendingIntent)
        }
    }
}