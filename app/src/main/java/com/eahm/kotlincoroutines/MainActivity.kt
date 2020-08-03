package com.eahm.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var count : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Getting Started!
         *
         * 1. Add the dependencies (See build.gradle)
         * 2. Creating the layout
         * 3. Adding the suspend function
         * 4. Adding the method of the button (launchCoroutines())
         */

        seekBarCoroutineAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                count = progress
                tvCoroutineCount.text = "$count coroutines"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private suspend fun performTask(taskNumber : Int) : Deferred<String> =
        coroutineScope.async (Dispatchers.Main){
            delay(5_000)
            return@async "coroutine finished $taskNumber"
        }

    fun launchCoroutines(view : View){
        (1..count).forEach{
            tvCoroutineStatus.text = "Coroutine Started $it"
            coroutineScope.launch (Dispatchers.Main){
                tvCoroutineStatus.text = performTask(it).await()
            }
        }
    }
}