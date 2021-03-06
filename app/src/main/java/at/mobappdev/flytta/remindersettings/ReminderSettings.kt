package at.mobappdev.flytta.remindersettings

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import at.mobappdev.flytta.R
import at.mobappdev.flytta.reminderlist.ReminderList
import ca.antonious.materialdaypicker.MaterialDayPicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class ReminderSettings : AppCompatActivity() {
    private lateinit var reminderName: EditText
    private lateinit var reminderFrom: EditText
    private lateinit var reminderTo: EditText
    private lateinit var reminderMin: TextView
    private lateinit var dayPicker: MaterialDayPicker
    private lateinit var minutes: SeekBar
    private lateinit var saveButton: Button
    private lateinit var auth: FirebaseAuth
    private var timesValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_settings)

        auth = FirebaseAuth.getInstance()
        reminderName = findViewById(R.id.reminderName)
        reminderFrom = findViewById(R.id.reminderFrom)
        reminderTo = findViewById(R.id.reminderTo)
        reminderMin = findViewById(R.id.intervalMin)
        dayPicker = findViewById(R.id.dayPicker)
        minutes = findViewById(R.id.seekBar)
        saveButton = findViewById(R.id.saveButton)
        minutes.max = 180
        dayPicker.locale = Locale.UK

        minutes.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress == 0){
                    reminderMin.setTextColor(Color.parseColor("#FF0000"))
                } else {
                    reminderMin.setTextColor(Color.parseColor("#000000"))
                }
                reminderMin.text = getString(R.string.minutes, progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                reminderMin.text = getString(R.string.minutes, seekBar?.progress.toString())
                allSettingsDone()
            }
        })

        reminderFrom.setOnClickListener {
            onTimePicked(reminderFrom, "From: ")
        }

        reminderTo.setOnClickListener {
            onTimePicked(reminderTo, "To: ")
        }

        dayPicker.setDaySelectionChangedListener {
            allSettingsDone()
        }

        reminderName.addTextChangedListener {
            allSettingsDone()
        }

        saveButton.setOnClickListener {
            saveReminderToDB()
        }
    }

    /**
     * Firebase Firestore
     * storing reminder in db
     * collection:users, document: userId, collection: reminder, document: generated id
     */
    private fun saveReminderToDB() {
        val db = Firebase.firestore
        val userId = auth.uid
        val reminder = hashMapOf(
            "name" to reminderName.text.toString(),
            "from" to reminderFrom.text.toString().split(" ")[1],
            "to" to reminderTo.text.toString().split(" ")[1],
            "minutes" to minutes.progress.toString(),
            "days" to dayPicker.selectedDays
        )

        if (userId != null) {
            db.collection("users").document(userId).collection("reminder")
                .add(reminder)
                .addOnSuccessListener {
                    Log.i("ReminderSettings", "Reminder added to DB")
                }
                .addOnFailureListener { e ->
                    Log.i("ReminderSettings", "Error adding reminder to DB", e)
                }
        }

        val intent = Intent(this, ReminderList::class.java) //going back to reminder list
        startActivity(intent)
    }

    /**
     * for enabling save button only if every input is valid
     */
    private fun allSettingsDone() {
        saveButton.isEnabled =
            dayPicker.selectedDays.isNotEmpty() && reminderTo.text.isNotEmpty() && reminderFrom.text.isNotEmpty() && reminderName.text.isNotEmpty() && minutes.progress != 0 && timesValid
        if(saveButton.isEnabled){
            saveButton.backgroundTintList = this.resources.getColorStateList(R.color.accentLightBlue)
        } else {
            saveButton.backgroundTintList = this.resources.getColorStateList(R.color.lightgrey)
        }
    }

    /**
     * showing timer picker dialog and setting time
     * called from edittext from/to listener
     * reminder: edittext-widget, text: From:/To:
     */
    private fun onTimePicked(reminder: EditText, text: String) {
        val calendar = Calendar.getInstance()
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val time = SimpleDateFormat("HH:mm", Locale.GERMAN).format(calendar.time)
                areTimesValid(time, text, reminder)
            }
        TimePickerDialog(
            this,
            timeSetListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    /**
     * checks if picked times are valid and calls allSettingsDone()
     * currentTime: picked time (eg. 9:17), currentText: From:/To:, reminder: edittext-widget
     */
    private fun areTimesValid(currentTime: String, currentText: String, reminder: EditText) {
        reminder.setText(getString(R.string.time, currentText,currentTime))
        if (currentText == "To: " && reminderFrom.text.toString() != "") {
            val start = reminderFrom.text.toString().split(" ")[1] //takes time from eg. From: 09:17
            if (start > currentTime) { //picked to-time is before from-time
                reminder.setTextColor(Color.parseColor("#FF0000"))
                timesValid = false //unused variable :(
                allSettingsDone()
            }

        } else if (currentText == "From: " && reminderTo.text.toString() != "") {
            val end = reminderTo.text.toString().split(" ")[1]
            if (end < currentTime) { //picked from-time is is after to-time
                reminder.setTextColor(Color.parseColor("#FF0000"))
                timesValid = false
                allSettingsDone()
            }
        }

        if (reminderTo.text.toString() != "" && reminderFrom.text.toString() != "") {
            val start = reminderFrom.text.toString().split(" ")[1]
            val end = reminderTo.text.toString().split(" ")[1]
            if (end > start) { //from-time is before-start time
                reminderTo.setTextColor(Color.parseColor("#000000"))
                reminderFrom.setTextColor(Color.parseColor("#000000"))
                timesValid = true
                allSettingsDone()
            }
        }
    }
}
