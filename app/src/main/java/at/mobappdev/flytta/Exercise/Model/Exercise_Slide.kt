package at.mobappdev.flytta.Exercise.Model

import com.google.gson.annotations.SerializedName

data class Exercise_Slide(val title : String,
                          val des : String,
                          val image: Int,
                          val time: Int = 30){

}