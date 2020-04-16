package at.mobappdev.flytta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView
import android.widget.Toast


class StepCounter : AppCompatActivity(), SensorEventListener {

    private var sensorManager:SensorManager? = null
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    //STEPCOUNTER
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // auto generated method for sensorlistener
    }

    override fun onSensorChanged(event: SensorEvent) {
        val text = findViewById<TextView>(R.id.stepCount)
        if(isRunning){
            text.text = event.values[0].toString()
        }
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        //exercises with steps or step-based reminder are only possible with sensor - TODO: implement check
        if(sensor == null){
            Toast.makeText(this, "No step count sensor", Toast.LENGTH_LONG).show()
        } else {
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause(){
        super.onPause()
        isRunning = false
        sensorManager?.unregisterListener(this)
    }
}
