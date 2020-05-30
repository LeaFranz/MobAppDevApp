package at.mobappdev.flytta.Exercise.Model

data class ExerciseInfo(var exerciseId : Int = 0,
                        var title : String = "",
                        var description : String ="",
                        var imagePath : String ="",
                        var time : Int = 30) {

}