package at.mobappdev.flytta.Exercise.Model

import android.content.ContentValues.TAG
import android.content.Context
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import at.mobappdev.flytta.R
import at.mobappdev.flytta.history.ExerciseUserData
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.row.view.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import androidx.core.content.ContextCompat.getSystemService as getSystemService

class ExerciseList (private var arrayList: MutableList<ExerciseInfo>, val context :Context): RecyclerView.Adapter<ExerciseList.ViewHolder>() {


    //sets Values in every row from Exercises
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        fun binItems(model:ExerciseInfo){
            val storageReference = Firebase.storage.reference.child("Exercises/${model.imagePath}.png")
            itemView.tv_title.text = model.title
            itemView.tv_description.text = model.description
            itemView.text_view.text = model.time.toString() + " sec"
            when (model.groupId) {
                1 -> {
                    itemView.groupId.text = "Shoulder"
                }
                2 -> {
                    itemView.groupId.text = "Legs"
                }
                3 -> {
                    itemView.groupId.text = "Hands"
                }
                4 -> {
                    itemView.groupId.text = "Others"
                }
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
        fun startTimer(time : Int, item : View){
            var counter : Long = time.toLong()
            var displayTime : Int = time
            val timer = object: CountDownTimer(counter*1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    displayTime--
                    item.text_view.text = displayTime.toString()
                }
                override fun onFinish() {
                    item.text_view.text = time.toString()
                }
            }
            timer.start()
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
                    Toast.makeText(context , "Saved Exercise successfully!", Toast.LENGTH_LONG)
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
        val zoneId: ZoneId = ZoneId.of("Europe/Paris")
        val today = LocalDate.now(zoneId)
        val currentUser = FirebaseAuth.getInstance().uid
        if(currentUser!=null){
            val exerciseUserData  = ExerciseUserData(exerciseGroupId, today.toString(), currentUser)
            Log.d("main", "current user uid ${FirebaseAuth.getInstance().uid}")

            val db = FirebaseFirestore.getInstance()
            db.collection("userExerciseData")
                .document()
                .set(exerciseUserData)
                .addOnSuccessListener {
                    Toast.makeText(context , "Saved Exercise successfully!", Toast.LENGTH_LONG).show()
                    Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
    }

    private fun vibrateFound(){
        val vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibe.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            Toast.makeText(context, "Cannot use Vibration", Toast.LENGTH_SHORT).show()
        }
    }


}