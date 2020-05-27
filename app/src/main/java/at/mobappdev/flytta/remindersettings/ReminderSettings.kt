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
import at.mobappdev.flytta.R
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
            TimerUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context:Context){
            val intent = Intent(context, TimerReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManger.cancel(pendingIntent)
            TimerUtil.setAlarmSetTime(0, context) //means that the alarm is not set
        }

        val nowSeconds:Long
            get() = Calendar.getInstance().timeInMillis/1000


    }

    enum class TimerState {
        Stopped, Paused, Running
    }

    lateinit var startButton: Button
    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.Stopped

    //L means long
    private var secondsRemaining = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_settings)

        startButton = findViewById(R.id.startTimerButton)
        startButton.setOnClickListener {
            startTimer()
            timerState = TimerState.Running
            Log.i("countdown", "on start")
        }
    }

    //from lifecycle
    override fun onResume() {
        super.onResume()
        initTimer()
        removeAlarm(this)
    }

    //called on home button
    override fun onPause() {
        super.onPause()
        if (timerState == TimerState.Running) {
            timer.cancel()
            val wakeupTime = setAlarm(this, nowSeconds, secondsRemaining)
        } else if (timerState == TimerState.Paused) {
            //TODO: show notification
        }

        TimerUtil.setPreviousTimerLength(this, timerLengthSeconds)
        TimerUtil.setSecondsRemaining(this, secondsRemaining)
        TimerUtil.setTimerState(timerState, this)
    }

    //called from on resume
    private fun initTimer() {
        timerState = TimerUtil.getTimerState(this)

        if (timerState == TimerState.Stopped) {
            setNewTimerLength()
        } else {
            //if paused bc app closed - continue
            setPreviousTimerLength()
        }

        //was already running before
        if (timerState == TimerState.Running || timerState == TimerState.Paused) {
            secondsRemaining = TimerUtil.getSecondsRemaining(this)
        } else {
            secondsRemaining = timerLengthSeconds
        }

        val alarmSetTime = TimerUtil.getAlarmSetTime(this)
        if(alarmSetTime > 0){
            secondsRemaining -= nowSeconds - alarmSetTime
        }

        if(alarmSetTime <= 0){
            onTimerFinished()
        } else if(timerState == TimerState.Running){
            startTimer()
        }

        updateCountdown()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped
        setNewTimerLength()

        TimerUtil.setSecondsRemaining(this, timerLengthSeconds)
        secondsRemaining = timerLengthSeconds
        updateCountdown()
        Log.i("countdown: ", "finished")
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
        val lengthInMinutes = TimerUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = TimerUtil.getPreviousTimerLength(this)
    }

    private fun updateCountdown() {
        val minutes = secondsRemaining/60
        val secondsInMinute = secondsRemaining - minutes*60
        val secondStr = secondsInMinute.toString()
        Log.i("countdown: ", "time: "+minutes+" "+secondsInMinute+" "+secondStr)
    }

}
