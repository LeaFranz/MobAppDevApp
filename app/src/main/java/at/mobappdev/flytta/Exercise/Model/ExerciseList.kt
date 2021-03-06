package at.mobappdev.flytta.Exercise.Model

import android.content.ContentValues.TAG
import android.content.Context
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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
import kotlin.concurrent.timer
import androidx.core.content.ContextCompat.getSystemService as getSystemService

class ExerciseList (private var arrayList: MutableList<ExerciseInfo>, val context :Context): RecyclerView.Adapter<ExerciseList.ViewHolder>() {


    //sets Values in every row from Exercises
    var allreadyClicked : Boolean = false

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        fun binItems(model:ExerciseInfo){
            val storageReference = Firebase.storage.reference.child("Exercises/${model.imagePath}.png")
            itemView.tv_title.text = model.title
            itemView.tv_description.text = model.description
            itemView.text_view.text = model.time.toString()
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
            /**
             * Glide getting the pictures from Firestorage
             */
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
    /**
     * needed for RecyclerView
     * Inflating" a view means taking the layout XML and parsing it to create the view and viewgroup
     */
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
                if(holder != null && !allreadyClicked){
                    timer(holder.itemView.text_view, arrayList[position].groupId)
                }else if(allreadyClicked){
                    var toast : Toast = Toast.makeText(context , "Exercise not finished! \n Wait until Timer is up.", Toast.LENGTH_SHORT)
                    toast.view.setBackgroundColor(ContextCompat.getColor(context, R.color.warning))
                    toast.show()
                }
            }
        }
    }

    /**
     * Timer for Exercises
     * OnTick: every time a second is over change the countNumber
     * OnFinish: if counter at 0 then set countNumber to original
     */
    private fun timer(text_view: TextView, exerciseId: Int){
        vibrateFound()
        var time : Int = text_view.text.toString().toInt()
        var timeValue : Long = time.toLong()*1000L
        var initialValue : Int = time
        if(time != null){
            allreadyClicked = true
            val timer = object: CountDownTimer(timeValue, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    text_view.text = time.toString()
                    time--
                }
                override fun onFinish() {
                    text_view.text = initialValue.toString()
                    saveExercise(exerciseId)
                    vibrateFound()
                    allreadyClicked = false
                }
            }
            timer.start()
        }
    }


    /**
     * write to DB when exercise is finished - needed for History
     * minimum SDK 26
     */
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

    /**
     * Checking if SDK is above 26
     * Virbate not available before
     */
    private fun vibrateFound(){
        if (Build.VERSION.SDK_INT >= 26) {
            val vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibe.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            Toast.makeText(context, "Cannot use Vibration", Toast.LENGTH_SHORT).show()
        }
    }


}