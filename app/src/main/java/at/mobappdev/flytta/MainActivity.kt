package at.mobappdev.flytta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToStepCounter(view: View){
        val intent = Intent(this, StepCounter::class.java)
        startActivity(intent)
    }

    fun goToHistory(view: View){
        val intent = Intent(this, History::class.java)
        startActivity(intent)
    }
}
