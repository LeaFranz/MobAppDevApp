package at.mobappdev.flytta.Exercise

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import at.mobappdev.flytta.Exercise.Model.ExerciseInfo
import at.mobappdev.flytta.Exercise.Model.ExerciseList
import at.mobappdev.flytta.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_exercises.*



class Exercises : AppCompatActivity() {

    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        db = FirebaseFirestore.getInstance()
        getAllDataFromCollection()
    }

    /**
     * Getting allExercises from Firestore
     * ifSuccesfull createrecycleriew
     */
    private fun getAllDataFromCollection(){
        val exerciseList : MutableList<ExerciseInfo> = ArrayList()
        val db = Firebase.firestore
        db.collection("exerciseData")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val currentDbObject = document.toObject(ExerciseInfo::class.java)
                    exerciseList.add(createExerciseInfoObject(currentDbObject))

                }
                val exerciseListFinal = ExerciseList(exerciseList, this)
                recyclerViewExercises?.layoutManager = LinearLayoutManager(this)
                recyclerViewExercises?.adapter = exerciseListFinal
            }
            .addOnFailureListener { exception ->
                Log.w("main activity", "Error getting documents: ", exception)
                Toast.makeText(this, "Unable to load Data!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createExerciseInfoObject(currentDBObject:ExerciseInfo) : ExerciseInfo{
        return ExerciseInfo(currentDBObject.exerciseId, currentDBObject.title, currentDBObject.description, currentDBObject.imagePath, currentDBObject.time, currentDBObject.groupId)
    }
}
