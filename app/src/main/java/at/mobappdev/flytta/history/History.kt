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

    private var xVal : Float = 0.45f
    private var exerciseUserDataWeekly : MutableList<ExerciseUserData> = ArrayList()
    private var yValueGroupTemp : ArrayList<BarEntry> = ArrayList()
    private var mon : MutableList<ExerciseUserData> = ArrayList()
    private var tue : MutableList<ExerciseUserData> = ArrayList()
    private var wed : MutableList<ExerciseUserData> = ArrayList()
    private var thu : MutableList<ExerciseUserData> = ArrayList()
    private var fri : MutableList<ExerciseUserData> = ArrayList()
    private var sat : MutableList<ExerciseUserData> = ArrayList()
    private var sun : MutableList<ExerciseUserData> = ArrayList()
    private lateinit var previousMon : LocalDate
    private lateinit var previousTue : LocalDate
    private lateinit var previousWed : LocalDate
    private lateinit var previousThu : LocalDate
    private lateinit var previousFri : LocalDate
    private lateinit var previousSat : LocalDate
    private lateinit var previousSun : LocalDate
    private var ex1:Int = 0
    private var ex2:Int = 0
    private var ex3:Int = 0
    private var ex4:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateMon()
        }else{
            Toast.makeText(this, "Can not open History Data!", Toast.LENGTH_SHORT).show()
        }
        getAllExerciseData()
    }
    /**
    Build.VERSION_CODES.O - is a reference to API level 26 (Android Oreo which is Android 8)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateMon(){
        val zoneId : ZoneId = ZoneId.of("Europe/Paris")
        val today = LocalDate.now(zoneId)
        previousMon  = today.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        previousTue = today.with(TemporalAdjusters.previous(DayOfWeek.TUESDAY))
        previousWed = today.with(TemporalAdjusters.previous(DayOfWeek.WEDNESDAY))
        previousThu  = today.with(TemporalAdjusters.previous(DayOfWeek.THURSDAY))
        previousFri = today.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY))
        previousSat = today.with(TemporalAdjusters.previous(DayOfWeek.SATURDAY))
        previousSun = today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))
        getTodaysWeekDay(today)
    }

    /**
     * Which day is today - so we get the Historydata from this day
     */
    private fun getTodaysWeekDay(today : LocalDate){
        when (today.dayOfWeek.toString()) {
            "MONDAY" -> {
                previousMon = today
            }
            "TUESDAY" -> {
                previousTue  = today
            }
            "WEDNESDAY" -> {
                previousWed = today
            }
            "THURSDAY" -> {
                previousThu = today
            }
            "FRIDAY" -> {
                previousFri = today
            }
            "SATURDAY" -> {
                previousSat = today
            }
            "SUNDAY" -> {
                previousSun = today
            }
        }
    }

    /**
    getting all ExerciseData from Firestore
    getting userspecific
     */
    private fun getAllExerciseData(){
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

    /**
     * MPAndroid needs sorted Data - every week day has its own List
     */
    private fun sortdata(){
        for(exercise in exerciseUserDataWeekly){
            when (exercise.exerciseTimestamp) {
                this.previousMon.toString() -> {
                    mon.add(exercise)
                }
                this.previousTue.toString() -> {
                    tue.add(exercise)
                }
                this.previousWed.toString() -> {
                    wed.add(exercise)
                }
                this.previousThu.toString() -> {
                    thu.add(exercise)
                }
                this.previousFri.toString() -> {
                    fri.add(exercise)
                }
                this.previousSat.toString() -> {
                    sat.add(exercise)
                }
                this.previousSun.toString() -> {
                    sun.add(exercise)
                }
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
        when (exercise.exerciseGroupId) {
            1 -> {
                ex1++
            }
            2 -> {
                ex2++
            }
            3 -> {
                ex3++
            }
            4 -> {
                ex4++
            }
        }
    }

    /**
     * adding values to MPAndroidChart
     */
    private fun addEntriesToChart(){
        yValueGroupTemp.add(BarEntry(xVal, floatArrayOf(ex1.toFloat(), ex2.toFloat(), ex3.toFloat(), ex4.toFloat())))
        xVal++
    }

    private fun emptyAllEx(){
        ex1 = 0
        ex2 = 0
        ex3 = 0
        ex4 = 0
    }

    private fun populateGraphData() {

        val barChartView:BarChart = findViewById(R.id.chartConsumptionGraph)

        val barWidth = 0.50f

        val xAxisValues = ArrayList<String>()
        xAxisValues.add("Mo")
        xAxisValues.add("Thu")
        xAxisValues.add("Wen")
        xAxisValues.add("Thur")
        xAxisValues.add("Fri")
        xAxisValues.add("Sat")
        xAxisValues.add("Sun")

        // draw the graph
        val barDataSet1: BarDataSet
        val yValueGroup1: ArrayList<BarEntry> = yValueGroupTemp

        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.setColors(-23296, -39017, -8612127, -5905976)
        barDataSet1.label = "Exercise1"
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(true)

        val barData = BarData(barDataSet1)

        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        barChartView.data = barData
        barChartView.barData.barWidth = barWidth
        barChartView.xAxis.axisMinimum = 0f
        barChartView.xAxis.axisMaximum = 7f
        //   barChartView.setFitBars(true)
        barChartView.data.isHighlightEnabled = false
        barChartView.invalidate()

        // set bar label
        val legend = barChartView.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.formSize = 20f

        val legenedEntries = arrayListOf<LegendEntry>()

        legenedEntries.add(LegendEntry("Shoulder", Legend.LegendForm.CIRCLE, 8f, 8f, null, -29696))
        legenedEntries.add(LegendEntry("Legs", Legend.LegendForm.CIRCLE, 8f, 8f, null, -39017))
        legenedEntries.add(LegendEntry("Hands", Legend.LegendForm.CIRCLE, 8f, 8f, null, -8612127))
        legenedEntries.add(LegendEntry("Others", Legend.LegendForm.CIRCLE, 8f, 8f, null, -5905976))

        legend.setCustom(legenedEntries)

        legend.yOffset = 10f
        legend.xOffset = 15f
        legend.yEntrySpace = 2f
        legend.textSize = 15f

        val xAxis = barChartView.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 9f

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        xAxis.labelCount = 7
        xAxis.mAxisMaximum = 7f
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 2f
        xAxis.spaceMax = 2f

        barChartView.setVisibleXRangeMaximum(7f)
        barChartView.setVisibleXRangeMinimum(7f)
        barChartView.isDragEnabled = true

        //Y-axis
        barChartView.axisRight.isEnabled = false
        barChartView.setScaleEnabled(true)

        val leftAxis = barChartView.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 1f
        leftAxis.axisMinimum = 0f


        barChartView.data = barData
        //sets x bar range
        barChartView.setVisibleXRange(1f, 7f)

    }
}