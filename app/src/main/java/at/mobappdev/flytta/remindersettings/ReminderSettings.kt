package at.mobappdev.flytta.remindersettings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import at.mobappdev.flytta.R
import io.opencensus.stats.Aggregation
import java.sql.Time

class ReminderSettings : AppCompatActivity() {

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
    }

    //called on home button
    override fun onPause() {
        super.onPause()
        if (timerState == TimerState.Running) {
            timer.cancel()
            //TODO: background timer
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

        //TODO: change secondsremaing according to where the background timer left of

        //resume where we left of
        if (timerState == TimerState.Running) {
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
