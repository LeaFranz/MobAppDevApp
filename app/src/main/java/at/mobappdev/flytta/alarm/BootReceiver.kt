package at.mobappdev.flytta.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import at.mobappdev.flytta.reminderlist.ReminderListItem
import at.mobappdev.flytta.remindersettings.ReminderPrefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//for starting alarm again after device restart
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            Log.i("Bootreceiver", "Alarm gets startet after device reboot")
            val auth = FirebaseAuth.getInstance()
            val db = Firebase.firestore
            val userId = auth.uid

            /**
             * Firebase Firestore
             * getting all reminder from user from firestore
             */
            if (userId != null) {
                db.collection("users").document(userId).collection("reminder")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.i(
                                "Bootreceiver",
                                "Getting reminder data was successful after reboot"
                            )

                            //checking which reminder was active before
                            if (ReminderPrefs.getSwitchActive(
                                    document.id,
                                    context
                                )
                            ) {
                                val days =
                                    document.data["days"] as ArrayList<*>
                                val item = ReminderListItem(
                                    document.id,
                                    document.data["name"].toString(),
                                    document.data["minutes"].toString(),
                                    days,
                                    document.data["from"].toString(),
                                    document.data["to"].toString()
                                )
                                //setting daily alarm again
                                Alarms.setDailyAlarm(context, item)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(
                            "Bootreceiver",
                            "Error getting reminder data after reboot: ",
                            exception
                        )
                    }
            }
        }

    }
}