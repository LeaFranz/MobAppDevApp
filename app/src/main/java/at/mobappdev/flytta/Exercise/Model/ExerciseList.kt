package at.mobappdev.flytta.Exercise.Model

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
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

class ExerciseList (var arrayList: MutableList<ExerciseInfo>, val context :Context): RecyclerView.Adapter<ExerciseList.ViewHolder>() {

    private var progressBar: ProgressBar? = null
    private var i = 0
    private var txtView: TextView? = null
    private val handler = Handler()

    //sets Values in every row from Exercises
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private var storageFb : FirebaseStorage = Firebase.storage

        fun binItems(model:ExerciseInfo){
            val storageReference = Firebase.storage.reference.child("Exercises/${model.imagePath}.png")
            itemView.tv_title.text = model.title
            itemView.tv_description.text = model.description

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
                if (position == 0) {
                    saveExercise(arrayList.get(0).groupId)
                } else if (position == 1) {
                    saveExercise(arrayList.get(1).groupId)
                } else if (position == 2) {
                    saveExercise(arrayList.get(2).groupId)
                } else if (position == 3) {
                    saveExercise(arrayList.get(3).groupId)
                } else if (position == 4) {
                    saveExercise(arrayList.get(4).groupId)
                }else if (position == 5) {
                    saveExercise(arrayList.get(5).groupId)
                }else if (position == 6) {
                    saveExercise(arrayList.get(6).groupId)
                }else if (position == 7) {
                    saveExercise(arrayList.get(7).groupId)
                }else if (position == 8) {
                    saveExercise(arrayList.get(8).groupId)
                }else if (position == 9) {
                    saveExercise(arrayList.get(9).groupId)
                }else if (position == 10) {
                    saveExercise(arrayList.get(10).groupId)
                }else if (position == 11) {
                    saveExercise(arrayList.get(11).groupId)
                }else if (position == 12) {
                    saveExercise(arrayList.get(12).groupId)
                }else if (position == 13) {
                    saveExercise(arrayList.get(13).groupId)
                }else if (position == 14) {
                    saveExercise(arrayList.get(14).groupId)
                }
            }
        }
    }


    //write to DB when exercise is started - needed for History
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveExercise(exerciseGroupId : Int) {
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
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }



}