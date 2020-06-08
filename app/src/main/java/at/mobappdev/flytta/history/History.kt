package at.mobappdev.flytta.history

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import at.mobappdev.flytta.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import kotlin.collections.ArrayList


class History : AppCompatActivity() {

    var xVal : Float = 0.45f
    var exerciseUserDataWeekly : MutableList<ExerciseUserData> = ArrayList()
    lateinit var currentDayOfWeek : String
    var yValueGroupTemp : ArrayList<BarEntry> = ArrayList()
    var mon : MutableList<ExerciseUserData> = ArrayList()
    var tue : MutableList<ExerciseUserData> = ArrayList()
    var wed : MutableList<ExerciseUserData> = ArrayList()
    var thu : MutableList<ExerciseUserData> = ArrayList()
    var fri : MutableList<ExerciseUserData> = ArrayList()
    var sat : MutableList<ExerciseUserData> = ArrayList()
    var sun : MutableList<ExerciseUserData> = ArrayList()
    lateinit var previousMon : LocalDate
    lateinit var previousTue : LocalDate
    lateinit var previousWed : LocalDate
    lateinit var previousThu : LocalDate
    lateinit var previousFri : LocalDate
    lateinit var previousSat : LocalDate
    lateinit var previousSun : LocalDate
    var ex1:Int = 0
    var ex2:Int = 0
    var ex3:Int = 0
    var ex4:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateMon()
        }else{
            Toast.makeText(this, "Can not open History Data!", Toast.LENGTH_SHORT)
        }
        getAllExerciseData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateMon(){
        var zoneId : ZoneId = ZoneId.of("Europe/Paris")
        var today = LocalDate.now(zoneId)
        previousMon  = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        previousTue = today.with(TemporalAdjusters.previous(DayOfWeek.TUESDAY))
        previousWed = today.with(TemporalAdjusters.previous(DayOfWeek.WEDNESDAY))
        previousThu  = today.with(TemporalAdjusters.previous(DayOfWeek.THURSDAY))
        previousFri = today.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY))
        previousSat = today.with(TemporalAdjusters.previous(DayOfWeek.SATURDAY))
        previousSun = today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
        getTodaysWeekDay(today)
    }
    private fun getTodaysWeekDay(today : LocalDate){
        var currentDayOfWeek = today.dayOfWeek.toString()
        if(currentDayOfWeek=="MONDAY"){
            previousMon = today
        }else if(currentDayOfWeek=="TUESDAY"){
            previousTue  = today
        }else if(currentDayOfWeek=="WEDNESDAY"){
            previousWed = today
        }else if(currentDayOfWeek=="THURSDAY"){
            previousThu = today
        }else if(currentDayOfWeek == "FRIDAY"){
            previousFri = today
        }else if(currentDayOfWeek == "SATURDAY"){
            previousSat = today
        }else if(currentDayOfWeek == "SUNDAY"){
            previousSun = today
        }
    }

    fun getAllExerciseData(){
        val db = Firebase.firestore
        db.collection("userExerciseData")
            .whereEqualTo("userId", FirebaseAuth.getInstance().uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val exerciseUserData = document.toObject(ExerciseUserData::class.java)
                    exerciseUserDataWeekly.add(exerciseUserData)
                }
                sortdata()
            }
            .addOnFailureListener { exception ->
                Log.w("Database:", "Error getting documents: ", exception)
            }
    }

    private fun sortdata(){
        for(exercise in exerciseUserDataWeekly){
            if(exercise.exerciseTimestamp == this.previousMon.toString()){
                mon.add(exercise)
            }else if(exercise.exerciseTimestamp == this.previousTue.toString()){
                tue.add(exercise)
            }else if(exercise.exerciseTimestamp == this.previousWed.toString()){
                wed.add(exercise)
            }else if(exercise.exerciseTimestamp == this.previousThu.toString()){
                thu.add(exercise)
            }else if(exercise.exerciseTimestamp == this.previousFri.toString()){
                fri.add(exercise)
            }else if(exercise.exerciseTimestamp == this.previousSat.toString()){
                sat.add(exercise)
            }else if(exercise.exerciseTimestamp == this.previousSun.toString()){
                sun.add(exercise)
            }
        }
        gatherData()
    }

    private fun gatherData(){
        iterateAll(mon)
        iterateAll(tue)
        iterateAll(wed)
        iterateAll(thu)
        iterateAll(fri)
        iterateAll(sat)
        iterateAll(sun)
        populateGraphData()
    }

    private fun iterateAll(weekDay : MutableList<ExerciseUserData>){
        for(exercise in weekDay){
            count(exercise)
        }
        addEntriesToChart()
        emptyAllEx()
    }

    private fun count (exercise:ExerciseUserData){
        if(exercise.exerciseGroupId==1){
            ex1++
        } else if(exercise.exerciseGroupId==2){
            ex2++
        } else if(exercise.exerciseGroupId==3){
            ex3++
        } else if(exercise.exerciseGroupId==4){
            ex4++
        }
    }

    fun addEntriesToChart(){
        yValueGroupTemp.add(BarEntry(xVal, floatArrayOf(ex1.toFloat(), ex2.toFloat(), ex3.toFloat(), ex4.toFloat())))
        xVal++
    }

    private fun emptyAllEx(){
        ex1 = 0
        ex2 = 0
        ex3 = 0
        ex4 = 0
    }

    fun populateGraphData() {

        var barChartView:BarChart = findViewById<BarChart>(R.id.chartConsumptionGraph)

        val barWidth: Float = 0.50f
        val barSpace: Float = 0.07f
        val groupSpace: Float = 0.56f
        val groupCount = 7

        var xAxisValues = ArrayList<String>()
        xAxisValues.add("Mo")
        xAxisValues.add("Thu")
        xAxisValues.add("Wen")
        xAxisValues.add("Thur")
        xAxisValues.add("Fri")
        xAxisValues.add("Sat")
        xAxisValues.add("Sun")

        var yValueGroup1 = ArrayList<BarEntry>()

        // draw the graph
        var barDataSet1: BarDataSet
        yValueGroup1 = yValueGroupTemp

        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.setColors(-23296, -39017, -8612127, -5905976)
        barDataSet1.label = "Exercise1"
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(true)

        var barData = BarData(barDataSet1)

        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        barChartView.setData(barData)
        barChartView.getBarData().setBarWidth(barWidth)
        barChartView.getXAxis().setAxisMinimum(0f)
        barChartView.getXAxis().setAxisMaximum(7f)
        //   barChartView.setFitBars(true)
        barChartView.getData().setHighlightEnabled(false)
        barChartView.invalidate()

        // set bar label
        var legend = barChartView.legend
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        legend.setDrawInside(false)
        legend.formSize = 20f

        var legenedEntries = arrayListOf<LegendEntry>()

        legenedEntries.add(LegendEntry("Shoulder", Legend.LegendForm.CIRCLE, 8f, 8f, null, -29696))
        legenedEntries.add(LegendEntry("Legs", Legend.LegendForm.CIRCLE, 8f, 8f, null, -39017))
        legenedEntries.add(LegendEntry("Hands", Legend.LegendForm.CIRCLE, 8f, 8f, null, -8612127))
        legenedEntries.add(LegendEntry("Others", Legend.LegendForm.CIRCLE, 8f, 8f, null, -5905976))

        legend.setCustom(legenedEntries)

        legend.setYOffset(10f)
        legend.setXOffset(15f)
        legend.setYEntrySpace(2f)
        legend.setTextSize(15f)

        val xAxis = barChartView.getXAxis()
        xAxis.setGranularity(1f)
        xAxis.setGranularityEnabled(true)
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 9f

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setValueFormatter(IndexAxisValueFormatter(xAxisValues))

        xAxis.setLabelCount(7)
        xAxis.mAxisMaximum = 7f
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 2f
        xAxis.spaceMax = 2f

        barChartView.setVisibleXRangeMaximum(7f)
        barChartView.setVisibleXRangeMinimum(7f)
        barChartView.setDragEnabled(true)

        //Y-axis
        barChartView.getAxisRight().setEnabled(false)
        barChartView.setScaleEnabled(true)

        val leftAxis = barChartView.getAxisLeft()
        leftAxis.setValueFormatter(LargeValueFormatter())
        leftAxis.setDrawGridLines(false)
        leftAxis.setSpaceTop(1f)
        leftAxis.setAxisMinimum(0f)


        barChartView.data = barData
        //sets x bar range
        barChartView.setVisibleXRange(1f, 7f)

    }
}