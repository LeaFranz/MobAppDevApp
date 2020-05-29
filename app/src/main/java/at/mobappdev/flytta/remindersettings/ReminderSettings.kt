package at.mobappdev.flytta.remindersettings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.SeekBar
import android.widget.TextView
import at.mobappdev.flytta.R
import at.mobappdev.flytta.reminderlist.NotificationBuilder
import io.opencensus.stats.Aggregation
import java.sql.Time
import java.util.*

class ReminderSettings : AppCompatActivity() {

    //companion object because functions don't have a lot to do with timer
    companion object  {
        fun setAlarm(context: Context, nowSeconds : Long, secondsRemaining:Long):Long{
            val wakeUpTime = (nowSeconds+secondsRemaining) *1000 //because alarms use milliseconds
            val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerReceiver::class.java) //broadcastreceiver - can subscribe to it - listen to event
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManger.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            TimerUtil.setAlarmSetTime(nowSeconds)
            return wakeUpTime
        }

        fun removeAlarm(context:Context){
            val intent = Intent(context, TimerReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManger.cancel(pendingIntent)
            TimerUtil.setAlarmSetTime(0) //means that the alarm is not set
        }

        val nowSeconds:Long
            get() = Calendar.getInstance().timeInMillis/1000
    }

    enum class TimerState {
        Stopped, Paused, Running
    }

    private lateinit var startButton: Button
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 30L
    private var timerState = TimerState.Stopped
    private lateinit var seekBar : SeekBar
    lateinit var barText : TextView
    private var secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_settings)
        TimerUtil.setContext(this)
        TimerUtil.setSelectedLength(30) //default timer length

        seekBar = findViewById(R.id.seekBar)
        barText = findViewById(R.id.seekBarTextview)
        seekBar.max = 300
        seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                barText.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                barText.text = "Selected "+seekBar?.progress.toString()
                TimerUtil.setSelectedLength(seekBar?.progress!!.toInt()) //TODO: check if preferences are needed
                secondsRemaining = seekBar.progress.toLong()*60
                setNewTimerLength()
                updateCountdown()
            }
        })


        startButton = findViewById(R.id.startTimerButton)
        startButton.setOnClickListener {
            startTimer()
            timerState = TimerState.Running
        }
    }

    //instantly called when timer is opened
    override fun onResume() {
        super.onResume()
        initTimer()
        removeAlarm(this)
    }

    //called on leaving
    override fun onPause() {
        super.onPause()
        if (timerState == TimerState.Running) {
            timer.cancel()
            val wakeupTime = setAlarm(this, nowSeconds, secondsRemaining)
        } else if (timerState == TimerState.Paused) {
            //TODO: show notification
        }

        TimerUtil.setPreviousTimerLength(timerLengthSeconds)
        TimerUtil.setSecondsRemaining(secondsRemaining)
        TimerUtil.setTimerState(timerState)

    }

    //called from on resume
    private fun initTimer() {
        timerState = TimerUtil.getTimerState()

        if (timerState == TimerState.Stopped) {
            setNewTimerLength()
        } else {
            //if timer was closed - want to continue where we left of
            setPreviousTimerLength()
        }

        //was already running before
        if (timerState == TimerState.Running || timerState == TimerState.Paused) {
            secondsRemaining = TimerUtil.getSecondsRemaining()
        } else {
            secondsRemaining = TimerUtil.getAlarmSetTime()*60
        }

        val alarmSetTime = TimerUtil.getAlarmSetTime()
        if(alarmSetTime > 0){
            secondsRemaining -= nowSeconds - alarmSetTime
        }

        //this is for changing while running i guess
        //TODO: check and maybe delete
//        if(alarmSetTime <= 0){
//            onTimerFinished()
//        } else if(timerState == TimerState.Running){
//            startTimer()
//        }

        updateCountdown()
    }

    private fun onTimerFinished() {
        NotificationBuilder.sendNotification(this, "Get up and move!", "Tap here to get to your fun exercise :D")
        timerState = TimerState.Stopped
        setNewTimerLength()

        TimerUtil.setSecondsRemaining(timerLengthSeconds)
        secondsRemaining = timerLengthSeconds
        updateCountdown()
    }

    private fun startTimer() {
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdown()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = TimerUtil.getSelectedLength()
        timerLengthSeconds = (lengthInMinutes * 60L)
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = TimerUtil.getPreviousTimerLength()
    }

    private fun updateCountdown() {
        val minutes = secondsRemaining/60
        val secondsInMinute = secondsRemaining - minutes*60
        val secondStr = secondsInMinute.toString()
        Log.i("countdown: ", "time: "+minutes+" "+secondsInMinute+" "+secondStr)
    }

}
