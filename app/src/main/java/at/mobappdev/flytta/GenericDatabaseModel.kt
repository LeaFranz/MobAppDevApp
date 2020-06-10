package at.mobappdev.flytta

import android.content.ContentValues.TAG
import android.util.Log
import at.mobappdev.flytta.Exercise.Model.ExerciseInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GenericDatabaseModel{
    private lateinit var db: FirebaseFirestore

    //has to be called first!
    private fun initDatabase(){
        db = FirebaseFirestore.getInstance()
    }

    fun getAllDataFromCollection(collectionPath : String, exerciseClass : ExerciseInfo){
        initDatabase()
        val db = Firebase.firestore
        db.collection(collectionPath)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("main", "${document.id} => ${document.data}")
                    Log.d("main", "current user uid ${FirebaseAuth.getInstance().uid}")
                    val currentDbObject = document.toObject(exerciseClass::class.java)
                    println(currentDbObject.toString())
                }
            }
            .addOnFailureListener { exception ->
                Log.w("main activity", "Error getting documents: ", exception)
            }
    }

    //Mehtod to add all Exercises to Database (only used once)
    private fun addExercisesToDB(){
        initDatabase()
        val arrayList = ArrayList<ExerciseInfo>()
        arrayList.add(
            ExerciseInfo(
                1,
                "Shoulder shrugs",
                "Gently lift your shoulders.\n Let them slowly fall.\n You should feel tension being released as your shoulders drop.\n",
                "R.drawable.exercise_01",
                60
            )
        )
        arrayList.add(
            ExerciseInfo(
                2,
                "Shoulders",
                "Move your head from right to left and from left to right. Do this for 30 seconds!",
                "R.drawable.exercise_02",
                30
            )
        )
        arrayList.add(
            ExerciseInfo(
                13,
                "Neck rotations",
                "Keep your head upright.\n Gently turn your head from side to side.\n As you turn your head, try to move it past your shoulder.\n",
                "R.drawable.exercise_03",
                30
            )
        )
        arrayList.add(
            ExerciseInfo(
                3,
                "Back and forward bend",
                "Gently bend your head back and forward.",
                "R.drawable.exercise_04",
                30
            )
        )
        arrayList.add(
            ExerciseInfo(
                4,
                "Hand lifts",
                "Lift your hands.",
                "R.drawable.exercise_05",
                30
            )
        )
        arrayList.add(
            ExerciseInfo(
                5,
                "Head rotations",
                "Gently rotate your head from side to side.",
                "R.drawable.exercise_06",
                30
            )
        )
        arrayList.add(
            ExerciseInfo(
                6,
                "Leg stretch",
                "Stretch your Legs. Do this 30 seconds per side.",
                "R.drawable.exercise_07",
                60
            )
        )
        arrayList.add(
            ExerciseInfo(
                7,
                "Back stretch",
                "Stretch your back!",
                "R.drawable.exercise_08",
                30
            )
        )
        arrayList.add(
            ExerciseInfo(
                8,
                "Breathing",
                "Breathe slowly! Do this for one Minute.",
                "R.drawable.exercise_09",
                60
            )
        )
        arrayList.add(
            ExerciseInfo(
                9,
                "Shoulder stretch",
                "Slowly stretch your shoulders.",
                "R.drawable.exercise_10",
                30
            )
        )
        arrayList.add(
            ExerciseInfo(
                10,
                "Upper body and arm stretch",
                "This is an upper body and arm stretch.",
                "R.drawable.exercise_11",
                30
            )
        )
        arrayList.add(
            ExerciseInfo(
                11,
                "Lift your Hands",
                "This is as easy as it seems, just lift your hands and stretch",
                "R.drawable.exercise_12",
                30
            )
        )
        arrayList.add(
            ExerciseInfo(
                12,
                "Shoulder rotation",
                "Gently rotate your shoulders. \n 30 seconds counterclockwise and 30 seconds clockwise",
                "R.drawable.exercise_13",
                60
            )
        )

        for(exercise in arrayList){
            db.collection("exerciseData").document(exercise.exerciseId.toString())
                .set(exercise)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }
}
