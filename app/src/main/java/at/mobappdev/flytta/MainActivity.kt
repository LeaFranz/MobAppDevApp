package at.mobappdev.flytta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import at.mobappdev.flytta.Exercise.Exercises
import at.mobappdev.flytta.reminderlist.ReminderList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToHistory(view: View){
        val intent = Intent(this, History::class.java)
        startActivity(intent)
    }

    fun goToSettings(view:View){
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }

    fun goToExercises(view:View){
        val intent = Intent(this, Exercises::class.java)
        startActivity(intent)
    }

    fun goToReminderList(view: View){
        val intent = Intent(this, ReminderList::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
