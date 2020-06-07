package at.mobappdev.flytta.reminderlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import at.mobappdev.flytta.R
import at.mobappdev.flytta.remindersettings.ReminderSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_reminder_list.*


class ReminderList : AppCompatActivity(),
    ReminderListAdapter.OnItemClickListener {
    private val list: MutableList<ReminderListItem> = mutableListOf()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_list)
        val buttonInsert = findViewById<Button>(R.id.button_insert)
        auth = FirebaseAuth.getInstance()

        generateList()

        buttonInsert.setOnClickListener {
            onInsertReminderClicked()
        }
    }

    override fun onItemClicked(item: ReminderListItem, position: Int) {
        //function not needed
    }

    private fun onInsertReminderClicked() {
        val intent = Intent(this, ReminderSettings::class.java)
        startActivity(intent)
    }

    private fun generateList() {
        val db = Firebase.firestore
        val userId = auth.uid
        var counter = 0
        if (userId != null) {
            db.collection("users").document(userId).collection("reminder")
                .get()
                .addOnSuccessListener { result ->
                    if (result.documents.size == 0) {
                        findViewById<ProgressBar>(R.id.listProgressBar).visibility = View.INVISIBLE
                        val toast = Toast.makeText(this, "Add a reminder!", Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.show()
                    }
                    for (document in result) {
                        Log.i("Reminderlist", "Getting reminder data was successful")
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
                        list.add(item)
                        counter++
                        if (result.size() == counter) buildRecyclerView()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("reminder list", "Error getting reminder data: ", exception)
                    findViewById<ProgressBar>(R.id.listProgressBar).visibility = View.INVISIBLE
                }
        }
    }

    private fun buildRecyclerView() {
        recycler_view.adapter =
            ReminderListAdapter(list, this)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        findViewById<ProgressBar>(R.id.listProgressBar).visibility = View.INVISIBLE
    }
}
