package at.mobappdev.flytta.Exercise.Model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import at.mobappdev.flytta.R
import com.google.firebase.database.DatabaseReference

import kotlinx.android.synthetic.main.row.view.*

class ExerciseList (var arrayList: MutableList<ExerciseInfo>, val context :Context): RecyclerView.Adapter<ExerciseList.ViewHolder>() {

    private lateinit var database: DatabaseReference

    //sets Values in every row from Exercises
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun binItems(model:ExerciseInfo){
            itemView.tv_title.text = model.title
            itemView.tv_description.text = model.description
            itemView.imageView.setImageResource(R.drawable.exercise_01)
        }
    }
    //needed for RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
       return arrayList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binItems(arrayList[position])
        holder.itemView.setOnClickListener{
            if(position == 0){

            }
        }
    }

    //write to DB when exercise is started - needed for History
    private fun saveExercise(exerciseId:Int, exerciseName:String, timestamp:Int, userId:String) {
/*        database = Firebase.database.reference
        val exercise = ExerciseInfo(exerciseId, exerciseName, timestamp, userId)
        database.child("exerciseData").child(userId).setValue(exercise)*/
    }
}