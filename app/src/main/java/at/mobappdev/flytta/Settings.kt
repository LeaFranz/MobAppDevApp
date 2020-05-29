package at.mobappdev.flytta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import at.mobappdev.flytta.reminderlist.NotificationBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        logOutButton.setOnClickListener {
            logOut()
        }

        val notifyButton = findViewById<Button>(R.id.notifyButton)
        notifyButton.setOnClickListener {
            NotificationBuilder.sendNotification(this, "Test Notification", "This is a test notification.")
        }

        getUsernameFromDB()
    }

    fun goToStepCounter(view: View){
        val intent = Intent(this, StepCounter::class.java)
        startActivity(intent)
    }

    private fun logOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent("at.mobappdev.flytta.login.RegisterActivity")
        startActivity(intent)
    }

    //mainly a demonstration of how you get data from the firestore
    private fun getUsernameFromDB(){
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("main", "${document.id} => ${document.data}")
                    Log.d("main", "current user uid ${FirebaseAuth.getInstance().uid}")
                    val user = document.toObject(User::class.java)
                    if(FirebaseAuth.getInstance().uid == user.id){
                        usernameTextView.text = user.username.toString()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w("main activity", "Error getting documents: ", exception)
            }
    }

    data class User(
        var id: String = "",
        var email: String = "",
        var username: String = ""
    )
}
