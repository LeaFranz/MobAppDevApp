package at.mobappdev.flytta.Exercise.Model

import com.google.gson.annotations.SerializedName

data class Exercise_Slide(@SerializedName ("id") var exerciseId: Int = 0,
                          var title: String = "untitled",
                          var imgSrc: String = ""){

}