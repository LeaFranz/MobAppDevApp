package at.mobappdev.flytta.remindersettings

import android.content.Context
import android.content.SharedPreferences

class ReminderPrefs() {

    companion object {
        private const val PREF_NAME = "TimerPreferences"

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