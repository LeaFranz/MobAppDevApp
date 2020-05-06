package at.mobappdev.flytta.Exercise.Model

import com.google.gson.annotations.SerializedName

data class ExerciseList(@SerializedName("exercises")var exercises : List<Exercise_Slide>){
    
}