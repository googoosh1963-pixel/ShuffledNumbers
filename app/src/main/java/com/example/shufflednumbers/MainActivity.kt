package com.example.shufflednumbers

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : ComponentActivity() {
    private lateinit var numberText: TextView
    private lateinit var shuffleBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var startBtn: Button
    private lateinit var pauseBtn: Button
    private lateinit var timerText: TextView

    private val numbers = ArrayList<Int>()
    private var index = 0

    // Timer
    private var seconds = 0
    private var running = false
    private val handler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            if (running) {
                seconds += 1
                updateTimerText()
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberText = findViewById(R.id.numberText)
        shuffleBtn = findViewById(R.id.shuffleBtn)
        nextBtn = findViewById(R.id.nextBtn)
        startBtn = findViewById(R.id.startBtn)
        pauseBtn = findViewById(R.id.pauseBtn)
        timerText = findViewById(R.id.timerText)

        // initialize numbers 0..99
        resetNumbers()

        shuffleBtn.setOnClickListener { shuffleNumbers() }
        nextBtn.setOnClickListener { nextNumber() }
        startBtn.setOnClickListener { startTimer() }
        pauseBtn.setOnClickListener { pauseTimer() }

        // show first number
        showCurrentNumber()
        updateTimerText()
    }

    private fun resetNumbers() {
        numbers.clear()
        for (i in 0..99) numbers.add(i)
        numbers.shuffle()
        index = 0
    }

    private fun shuffleNumbers() {
        numbers.shuffle()
        index = 0
        showCurrentNumber()
    }

    private fun nextNumber() {
        if (numbers.isEmpty()) return
        index = (index + 1) % numbers.size
        showCurrentNumber()
    }

    private fun showCurrentNumber() {
        if (numbers.isEmpty()) {
            numberText.text = "--"
            return
        }
        val value = numbers[index]
        numberText.text = String.format(Locale.getDefault(), "%02d", value)
    }

    private fun startTimer() {
        if (!running) {
            running = true
            handler.postDelayed(timerRunnable, 1000)
        }
    }

    private fun pauseTimer() {
        running = false
        handler.removeCallbacks(timerRunnable)
    }

    private fun updateTimerText() {
        val hh = seconds / 3600
        val mm = (seconds % 3600) / 60
        val ss = seconds % 60
        timerText.text = String.format(Locale.getDefault(), "%02d:%02d:%02d", hh, mm, ss)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(timerRunnable)
    }

    override fun onResume() {
        super.onResume()
        if (running) handler.postDelayed(timerRunnable, 1000)
    }
}
