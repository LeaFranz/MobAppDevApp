package at.mobappdev.flytta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import at.mobappdev.flytta.alarm.NotificationBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_logout.*

class Logout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        getUsernameFromDB()

        logOutButton.setOnClickListener {
            logOut()
        }

        notifyButton.setOnClickListener {
            NotificationBuilder.sendNotification(
                this,
                "Test Notification",
                "This is a test notification :)"
            )
        }
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent("at.mobappdev.flytta.login.RegisterActivity")
        startActivity(intent)
    }

    private fun getUsernameFromDB() {
        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()
        val userId = auth.uid

        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { result ->
                    Log.i("Logout", "Getting userdata was successful")
                    greetingTextView.text =
                        getString(R.string.greeting, result.getString("username"))
                }
                .addOnFailureListener { exception ->
                    Log.w("Logout", "Error getting documents: ", exception)
                }
        }
    }

}
