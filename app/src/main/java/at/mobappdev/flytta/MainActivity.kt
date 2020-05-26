package at.mobappdev.flytta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import at.mobappdev.flytta.Exercise.Exercises
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(checkGooglePlayServices()){
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })
        } else {
            Log.w(TAG, "Device doesn't have google play services")
        }

    }

    //TODO: not used6
    fun runtimeEnableAutoInit() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }

    //checks if device has google play services enabled to receive notifications
    private fun checkGooglePlayServices(): Boolean {
        // 1
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        // 2
        return if (status != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Error")
            // ask user to update google play services and manage the error.
            false
        } else {
            // 3
            Log.i(TAG, "Google play services updated")
            true
        }
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
