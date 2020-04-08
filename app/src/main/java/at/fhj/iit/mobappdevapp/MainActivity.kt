package at.fhj.iit.mobappdevapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToPlanner(view : View){
        val intent = Intent(this, Planner::class.java)
        startActivity(intent)
    }

    fun goToHistory(view : View){
        val intent = Intent(this, History::class.java)
        startActivity(intent)
    }

    fun goToExercises(view : View){
        val intent = Intent(this, Exercises::class.java)
        startActivity(intent)
    }

    fun goToSettings(view : View){
        val intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }
}
