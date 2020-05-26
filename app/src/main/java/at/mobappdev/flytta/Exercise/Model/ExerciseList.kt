package at.mobappdev.flytta.Exercise.Model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import at.mobappdev.flytta.R
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_exercises.view.*
import kotlinx.android.synthetic.main.row.view.*

class ExerciseList (var arrayList: ArrayList<Exercise_Slide>, val context :Context):
    RecyclerView.Adapter<ExerciseList.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun binItems(model:Exercise_Slide){
            itemView.tv_title.text = model.title
            itemView.tv_description.text = model.des
            itemView.imageView.setImageResource(model.image)
        }
    }

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
                Toast.makeText(context, arrayList[position].title, Toast.LENGTH_LONG).show()
                timecounter(arrayList[position].time)
                saveInDataBase()
            }
            if(position == 1){
                Toast.makeText(context, "You didnt click second!", Toast.LENGTH_LONG).show()
            }
            if(position == 2){
                Toast.makeText(context, "You didnt click third!", Toast.LENGTH_LONG).show()
            }
            if(position == 3){
                Toast.makeText(context, "You didnt click fourth!", Toast.LENGTH_LONG).show()
            }
            if(position == 4){
                Toast.makeText(context, "You didnt click fifth!", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun timecounter(time : Int){
        //TODO implement timer for exercises
    }

    fun saveInDataBase(){
        //TODO implement save data to DB FireStore
    }
}