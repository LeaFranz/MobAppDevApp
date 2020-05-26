package at.mobappdev.flytta.Exercise

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import at.mobappdev.flytta.Exercise.Model.ExerciseList
import at.mobappdev.flytta.Exercise.Model.Exercise_Slide
import at.mobappdev.flytta.R
import kotlinx.android.synthetic.main.activity_exercises.*
import kotlinx.android.synthetic.main.activity_reminder_list.*

class Exercises : AppCompatActivity() {

    private lateinit var tvDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        var arrayList = addExercises()
        val exerciseList = ExerciseList(arrayList, this)

        recyclerViewExercises?.layoutManager = LinearLayoutManager(this)
        recyclerViewExercises?.adapter = exerciseList
    }
    private fun addExercises () : ArrayList<Exercise_Slide>{
        var arrayList = ArrayList<Exercise_Slide>()
        arrayList.add(Exercise_Slide("Shoulder shrugs",
            "Gently lift your shoulders.\n" +
                    "Let them slowly fall.\n" +
                    "You should feel tension being released as your shoulders drop.\n",
            R.drawable.exercise_01, 60))

        arrayList.add(Exercise_Slide("Shoulders", "Move your head from right to left " +
                "and from left to right. Do this for 30 seconds!", R.drawable.exercise_02))
        arrayList.add(Exercise_Slide("Neck rotations", "Keep your head upright.\n" +
                "Gently turn your head from side to side.\n" +
                "As you turn your head, try to move it past your shoulder.\n", R.drawable.exercise_03))
        arrayList.add(Exercise_Slide("Back", "", R.drawable.exercise_04))
        arrayList.add(Exercise_Slide("Hand lifts", "this is description", R.drawable.exercise_05))
        arrayList.add(Exercise_Slide("Head rotations", "this is description", R.drawable.exercise_06))
        arrayList.add(Exercise_Slide("Leg stretch", "this is description", R.drawable.exercise_07))
        arrayList.add(Exercise_Slide("Back stretch", "this is description", R.drawable.exercise_08))
        arrayList.add(Exercise_Slide("Breathing", "this is description", R.drawable.exercise_09))
        arrayList.add(Exercise_Slide("Shoulder stretch", "this is description", R.drawable.exercise_10))
        arrayList.add(Exercise_Slide("Upper body and arm stretch", "this is description", R.drawable.exercise_11))
        arrayList.add(Exercise_Slide("Unknown", "this is description", R.drawable.exercise_12))
        arrayList.add(Exercise_Slide("Shoulder rotation", "this is description", R.drawable.exercise_13))
        return arrayList
    }
}
