package at.mobappdev.flytta.remindersettings

import android.content.Context
import android.content.SharedPreferences


class TimerUtil {

    //members in companion objects are something like static members in java
    companion object {
        private val prefName = "TimerPreferences"

        //context to extract value from prefs
        fun getTimerLength(contex: Context): Int {
            //placeholder
            return 1
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "previous_timer_length"

        //to remember time length while timer is , only future timer changed
        fun getPreviousTimerLength(context:Context):Long{
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return sharedPref.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID,0)
        }

        fun setPreviousTimerLength(context:Context, seconds:Long){
            val editor: SharedPreferences.Editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "timer_state"

        fun getTimerState(context:Context):ReminderSettings.TimerState{
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            val indexOfState = sharedPref.getInt(TIMER_STATE_ID, 0)
            return ReminderSettings.TimerState.values()[indexOfState]
        }

        fun setTimerState(timerState: ReminderSettings.TimerState, context:Context){
            val editor: SharedPreferences.Editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit()
            val indexOfState = timerState.ordinal
            editor.putInt(TIMER_STATE_ID, indexOfState)
            editor.apply()
        }

        private const val SECONDS_REMAINING_ID = "seconds_remaining_id"

        //to remember time length while timer is , only future timer changed
        fun getSecondsRemaining(context:Context):Long{
            val sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return sharedPref.getLong(SECONDS_REMAINING_ID,0)
        }

        fun setSecondsRemaining(context:Context, seconds:Long){
            val editor: SharedPreferences.Editor = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }
    }
}