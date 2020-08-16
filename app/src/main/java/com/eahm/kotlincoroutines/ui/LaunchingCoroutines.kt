package com.eahm.kotlincoroutines.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.eahm.kotlincoroutines.R
import kotlinx.android.synthetic.main.activity_launching_coroutines.*
import kotlinx.coroutines.*

class LaunchingCoroutines : AppCompatActivity() {

    //region variables
    private var lineCounter = 0
    private val seconds = 5_000L
    /**
     * 2. Declare the coroutine scope
     *
     * Here we call the CoroutineScope method and pass the thread to
     * perform operations. In this case is the Main Thread.
     */
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val defaultScope = CoroutineScope(Dispatchers.Default)

    private val io = "Dispatchers.IO"
    private val main = "Dispatchers.Main"
    private val default = "Dispatchers.Default"

    private val messageIO = "DESCRIPTION:\n\t\tOptimized for disk and network IO\n\t\toff the main thread\nUSES:\n\t\tDatabase: Room will provide main-safety automatically if you use suspend functions, RxJava, or LiveData.\n\t\tReading/writing files\n\t\tNetworking: libraries such as Retrofit and Volley manage their own threads and do not require explicit main-safety in your code when used with Kotlin coroutines."
    private val messageMain = "DESCRIPTION:\n\t\tMain thread on Android, interact with the UI and perform light work\nUSES:\n\t\tCalling suspend functions\n\t\tCall UI functions\n\t\tUpdating LiveData"
    private val messageDefault = "DESCRIPTION:\n\t\tOptimized for CPU intensive work\n\t\toff the main thread\nUSES:\n\t\tSorting a list\n\t\tParsing JSON\n\t\tDiffUtils"

    //endregion variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launching_coroutines)

        /**
         * Getting Started
         * 1. Add the dependencies to use Kotlin Coroutines. Add the core library and the
         *    android library to have access to the android environment:
         *    org . jetbrains . kotlin -> kotlinx-coroutines-core and kotlinx-coroutines-android
         *
         * 2. Create the coroutine scope (See variables above)
         *
         * 3. Use the scope to launch a coroutine
         */

        btnLCCleanLog.setOnClickListener{
            setInformation("", add = false, count = false)
        }

        btnLCIO.setOnClickListener{
            setInformation("Running operations in IO. Wait $seconds seconds", true)
            ioScope.launch {
                fakeFetchOperation(io, messageIO)
            }
        }

        btnLCMain.setOnClickListener{
            setInformation("Running operations in Main. Wait $seconds seconds", true)
            mainScope.launch {
                fakeFetchOperation(main, messageMain)
            }
        }

        btnLCDefault.setOnClickListener{
            setInformation("Running operations in Default. Wait $seconds seconds", true)
            defaultScope.launch {
                fakeFetchOperation(default, messageDefault)
            }
        }

    }

    //region logic
    /**
     * Coroutines build upon regular functions by adding two new operations. In
     * addition to invoke (or call) and return, coroutines add suspend and resume.
     *      suspend — pause the execution of the current coroutine, saving all local variables
     *      resume — continue a suspended coroutine from the place it was paused
     *
     * This functionality is added by Kotlin by the suspend keyword on the function. You can
     * only call suspend functions from other suspend functions, or by using a coroutine builder
     * like launch to start a new coroutine.
     */
    private suspend fun fakeFetchOperation(dispatcherName :String, fakeResult : String) {
        // Suspend and resume work together to replace callbacks.
        // Coroutines will run on the main thread, and suspend does not mean background.

        delay(seconds) // Simulates a heavy operation

        /**
         * The heavy operation is done and we can resume our tasks.
         */
        showUIToast(dispatcherName, fakeResult)
    }

    /**
     * We change the coroutine scope to UI to being able to
     * show a user interface element (Toast)
     */
    private suspend fun showUIToast(dispatcherName : String, message : String){
        /**
         * withContext lets you control what thread any line of code executes on without
         * introducing a callback to return the result, and you can apply it to very small
         * functions like reading from your database or performing a network request.
         * A good practice is to use withContext to make sure every function is safe to be
         * called on any Dispatcher (including Main)
         *
         * It’s a really good idea to make every suspend function main-safe. If it does
         * anything that touches the disk, network, or even just uses too much CPU, use
         * withContext to make it safe to call from the main thread
         *
         * Performance: withContext is as fast as callbacks or RxJava for providing main safety.
         */
        withContext(Dispatchers.Main) {
            // Changed to Dispatchers.Main
            /* perform blocking UI here */
            //show("Completed: $dispatcherName")
            setInformation("Completed: ${dispatcherName} ------------  \n${message}\n", true)
        }
    }

    /**
     * Print a new message in the log container on the screen.
     */
    private fun setInformation(message: String, add : Boolean, count : Boolean = true){
        if(count) lineCounter++

        var finalMessage =  if(count) "$lineCounter $message" else message

        if(add){
            finalMessage = if(count) "$lineCounter ${message}\n${tvLCInformation.text}" else "${message}\n${tvLCInformation.text}"
        }

        tvLCInformation.text = finalMessage
    }

    /**
     * Show a Toast with a message
     */
    private fun show(message : String){
        Toast.makeText(this@LaunchingCoroutines, message, Toast.LENGTH_LONG).show()
    }

    //endregion logic
}