package at.mobappdev.flytta.Exercise.Model

import android.content.ContentValues.TAG
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import at.mobappdev.flytta.R
import at.mobappdev.flytta.history.ExerciseUserData
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

import kotlinx.android.synthetic.main.row.view.*
import java.io.File
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import androidx.core.content.ContextCompat.getSystemService as getSystemService

class ExerciseList (var arrayList: MutableList<ExerciseInfo>, val context :Context): RecyclerView.Adapter<ExerciseList.ViewHolder>() {

    lateinit var view : ViewHolder

    //sets Values in every row from Exercises
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        fun binItems(model:ExerciseInfo){
            val storageReference = Firebase.storage.reference.child("Exercises/${model.imagePath}.png")
            itemView.tv_title.text = model.title
            itemView.tv_description.text = model.description
            itemView.text_view.text = model.time.toString()
            if(model.groupId == 1){
                itemView.groupId.text = "Shoulder"
            }else if(model.groupId == 2){
                itemView.groupId.text = "Legs"
            }else if(model.groupId == 3){
                itemView.groupId.text = "Hands"
            }else if(model.groupId == 4){
                itemView.groupId.text = "Others"
            }
            storageReference.downloadUrl.addOnSuccessListener{Uri->
                val imageUrl = Uri.toString()
                Glide.with(itemView)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.initalexercise)
                    .error(R.drawable.initalexercise)
                    .fallback(R.drawable.initalexercise)
                    .into(itemView.imageView)
            }
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(holder != null){
                    timer(holder.itemView.text_view, position)
                }
            }
        }
    }

    private fun timer(text_view:TextView, exerciseId: Int){
        vibrateFound()
        var time : Int = text_view.text.toString().toInt()
        var timeValue : Long = time.toLong()*1000L
        var initialValue : Int = time
        if(time != null){
            val timer = object: CountDownTimer(timeValue, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    text_view.text = time.toString()
                    time--
                }

                override fun onFinish() {
                    text_view.text = initialValue.toString()
                    saveExercise(exerciseId)
                    vibrateFound()
                }
            }
            timer.start()
        }
    }


    //write to DB when exercise is started - needed for History
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveExercise(exerciseGroupId : Int) {
        vibrateFound()
        var zoneId: ZoneId = ZoneId.of("Europe/Paris")
        var today = LocalDate.now(zoneId)
        var currentUser = FirebaseAuth.getInstance().uid
        if(currentUser!=null){
            var exerciseUserData : ExerciseUserData = ExerciseUserData(exerciseGroupId, today.toString(), currentUser)
            Log.d("main", "current user uid ${FirebaseAuth.getInstance().uid}")
            var db = FirebaseFirestore.getInstance()
            db.collection("userExerciseData")
                .document()
                .set(exerciseUserData)
                .addOnSuccessListener {
                    Toast.makeText(context , "Saved Exercise successfully!", Toast.LENGTH_LONG).show()
                    Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }

    fun vibrateFound(){
        val vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibe.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            Toast.makeText(context, "Cannot use Vibration", Toast.LENGTH_SHORT).show()
        }
    }


}