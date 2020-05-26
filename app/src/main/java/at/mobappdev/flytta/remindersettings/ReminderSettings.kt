package at.mobappdev.flytta.remindersettings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import at.mobappdev.flytta.R

class ReminderSettings : AppCompatActivity() {
    lateinit var startButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_settings)

        startButton = findViewById(R.id.startTimerButton)
        startButton.setOnClickListener {
           //do something
        }
    }

}
