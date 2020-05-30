package at.mobappdev.flytta.history

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import at.mobappdev.flytta.R
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_history.*
import java.time.LocalDateTime

class History : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        button.setOnClickListener{
            getSingleData()
        }
    }

    fun getSingleData(){
        Firebase.database.reference
            .child("exerciseData")
            .child("1")
            .addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var map = p0.value as Map<String, Any>
                textView.text = map["exerciseId"].toString();
            }

        })
    }
    fun saveData(){
        var setEitTextString = textView.text.toString()
        var map = mutableMapOf<String, Any>()
        map["exerciseId"] = 1
        map["exerciseName"] = "nameme"
        map["time"] = 1590761701
        map["userId"] = "xyvl1Umgu5fr6dzpnBtSktPSVTM2"

        FirebaseDatabase.getInstance().reference
            .child("exerciseData")
            .child("1")
            .setValue(map)
    }
    fun updateData(){

    }
    fun deleteData(){

    }

}