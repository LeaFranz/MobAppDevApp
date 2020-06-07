package at.mobappdev.flytta.remindersettings

import android.content.Context
import android.content.SharedPreferences
import android.sax.EndElementListener
import at.mobappdev.flytta.reminderlist.ReminderListItem


class TimerUtil() {

    //members in companion objects are something like static members in java
    companion object {
        private const val PREF_NAME = "TimerPreferences"
        private lateinit var context: Context
        private const val PREVIOUS_TIMER_LENGTH_SECONDS = "previousTimerLength"
        private const val TIMER_STATE = "timerState"
        private const val SECONDS_REMAINING = "secondsRemaining"
        private const val ALARM_SET_TIME = "backgroundedTime"
        private const val SELECTED_LENGTH_MINUTES = "selectedLengthMinutes"

        fun setContext(con: Context) {
            context = con
        }


//        //to remember time length while timer is , only future timer changed
//        fun getPreviousTimerLength(): Long {
//            val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            return sharedPref.getLong(PREVIOUS_TIMER_LENGTH_SECONDS, 0)
//        }
//
//        fun setPreviousTimerLength(seconds: Long) {
//            val editor: SharedPreferences.Editor =
//                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
//            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS, seconds)
//            editor.apply()
//        }
//
//
//        fun getTimerState(): ReminderSettings.TimerState {
//            val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            val indexOfState = sharedPref.getInt(TIMER_STATE, 0)
//            return ReminderSettings.TimerState.values()[indexOfState]
//        }
//
//        fun setTimerState(timerState: ReminderSettings.TimerState) {
//            val editor: SharedPreferences.Editor =
//                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
//            val indexOfState = timerState.ordinal
//            editor.putInt(TIMER_STATE, indexOfState)
//            editor.apply()
//        }
//
//        fun getSecondsRemaining(): Long {
//            val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            return sharedPref.getLong(SECONDS_REMAINING, 0)
//        }
//
//        fun setSecondsRemaining(seconds: Long) {
//            val editor: SharedPreferences.Editor =
//                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
//            editor.putLong(SECONDS_REMAINING, seconds)
//            editor.apply()
//        }
//
//
//        fun getAlarmSetTime(): Long {
//            val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            return sharedPref.getLong(ALARM_SET_TIME, 0)
//        }
//
//        fun setAlarmSetTime(time: Long) {
//            val editor: SharedPreferences.Editor =
//                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
//            editor.putLong(ALARM_SET_TIME, time)
//            editor.apply()
//        }
//
//
//        fun getSelectedLength(): Int {
//            val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
//            return sharedPref.getInt(SELECTED_LENGTH_MINUTES, 0)
//        }

        fun setSelectedLength(min: Int) {
            val editor: SharedPreferences.Editor =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putInt(SELECTED_LENGTH_MINUTES, min)
            editor.apply()
        }

        fun setSwitchActive(active: Boolean, ID: String) {
            val editor: SharedPreferences.Editor =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putBoolean(ID, active)
            editor.apply()
        }

        fun getSwitchActive(ID: String, context:Context): Boolean {
            val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPref.getBoolean(ID, false)
        }

        fun setCurrentEndTime(timeEnd:String, context:Context){
            val editor: SharedPreferences.Editor =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putString("timeEnd", timeEnd)
            editor.apply()
        }

        fun getCurrentEndTime(context:Context):String? {
            val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPref.getString("timeEnd", "")
        }
    }
}