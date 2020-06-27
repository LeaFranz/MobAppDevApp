package at.mobappdev.flytta.remindersettings

import android.content.Context
import android.content.SharedPreferences

/**
 * class containing shared preferences
 * for saving key-value pairs on the device
 */
class ReminderPrefs {

    /**
     * companion object
     * function/property is tied to class rather than instance
     * singleton
     * accessed via the class name
     */
    companion object {
        private const val PREF_NAME = "TimerPreferences"

        /**
         * saving switch-state from reminder list
         * ID: reminder id
         */
        fun setSwitchActive(active: Boolean, ID: String, context:Context) {
            val editor: SharedPreferences.Editor =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            editor.putBoolean(ID, active)
            editor.apply()
        }

        fun getSwitchActive(ID: String, context:Context): Boolean {
            val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPref.getBoolean(ID, false)
        }

        /**
         * saves end-time of a daily-reminder in order to not send interval-reminder any more (IntervalAlarmReceiver)
         */
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