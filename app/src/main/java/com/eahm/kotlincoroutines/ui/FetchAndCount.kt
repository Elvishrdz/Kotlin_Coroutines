package com.eahm.kotlincoroutines.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.eahm.kotlincoroutines.R
import com.eahm.kotlincoroutines.interfaces.FetchMessageCallback
import kotlinx.android.synthetic.main.activity_fetch_and_count.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.random.Random

class FetchAndCount : AppCompatActivity() {

    //region variables
    private var useCoroutines = true

    private var tapCount = 0

    /**
     * Scope for our coroutines. They will perform the operations in the
     * Main thread.
     */
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val repository = Repository()

    //endregion variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_and_count)

        /**
         * Getting Started
         * 1. Add the coroutines-core library to the project. (See build.gradle)
         *      kotlinx-coroutines-core — Main interface for using coroutines in Kotlin
         *      kotlinx-coroutines-android — Support for the Android Main thread in coroutines
         *
         * 2. Create the layout design. Then react to the user touch input in the layout
         *    Then create the methods updateTaps() and refreshMessage()
         *
         * 3. Create a scope for our coroutine (See variables on top of this class). Assign the
         *    Main thread to our scope.
         *
         *   A. See updateTabs() to continue...
         *   B. See refreshMessage() to continue...
         */

        /**
         *  Coroutines Scopes
         *  In Kotlin, all coroutines run inside a CoroutineScope. A scope controls
         *  the lifetime of coroutines through its job. When you cancel the job of a scope,
         *  it cancels all coroutines started in that scope. On Android, you can use a
         *  scope to cancel all running coroutines when, for example, the user navigates
         *  away from an Activity or Fragment. Scopes also allow you to specify a default
         *  dispatcher. A dispatcher controls which thread runs a coroutine.
         *
         */

        /**
         * Important Information
         * 1. For your app to display to the user without any visible pauses, the main
         *    thread has to update the screen every 16ms or more, which is about 60 frames per second.
         * 2. In the end, Coroutines do the exact same thing as Callbacks: wait until a result is
         *    available from a long-running task and continue execution. However, in code they look
         *    very different.
         *
         */

        rootLayoutFC.setOnClickListener{
            // Use viewModel and liveData in real projects
            updateTaps()
            refreshMessage()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTaps(){
        if(useCoroutines){
            // Using Coroutines. The scope is the main thread (See coroutineScope variable above)
            coroutineScope.launch {
                tvFCStatus.text = "…"
                tapCount++

                /**
                  The function delay is a suspend function. (Check the icon at the left of the delay() method)
                  delay won't block the thread for one second. Instead, the dispatcher will schedule
                  the coroutine to resume in one second at the next statement.
                 */
                /*
              1. delay is now simulating another suspend function who can take a lot of time to complete
                 once the operation is done we can continue the operations below this method
                 */
                delay(1_000)

                /**
              2. here we continue the execution after the delay
                 */
                tvFCAmount.text = tapCount.toString()
                tvFCStatus.text = ""
            }
        }
        else {
            // Using Threads
            tapCount++
            Thread.sleep(1_000)
            tvFCAmount.text = tapCount.toString()
        }
    }

    private fun refreshMessage(){
        if(useCoroutines){
            coroutineScope.launch {
                try {
                    pbFC.visibility = View.VISIBLE

                    /**
                     *   refreshTitleWithCoroutines() is another suspend function so instead of
                     *   blocking the main thread  refreshMessage() will 'suspend' until the
                     *   result is ready
                     */
                    repository.refreshTitleWithCoroutines()

                    /**
                     *  Continue to execute after refreshTitleWithCoroutines() has done its job.
                     */
                    tvFCMessage.text = repository.message
                }
                catch (e : Exception){
                    Toast.makeText(this@FetchAndCount, e.message, Toast.LENGTH_SHORT).show()
                }
                finally {
                    pbFC.visibility = View.GONE
                }
            }
        }
        else {
            // Using callbacks
            pbFC.visibility = View.VISIBLE
            repository.refreshTitleWithCallbacks(object: FetchMessageCallback {
                override fun onCompleted() {
                    pbFC.visibility = View.GONE
                    tvFCMessage.text = repository.message
                }

                override fun onError(error: Exception) {
                    Toast.makeText(this@FetchAndCount, error.message, Toast.LENGTH_SHORT).show()
                    pbFC.visibility = View.GONE
                }
            })
        }
    }

    /**
     * Fake Repository to obtain messages. Simulates a network request for learning purposes.
     */
    inner class Repository {
        var message : String = ""
        private val messages = listOf(
            "Welcome to Coroutines with Kotlin",
            "Heads up!",
            "suspend fun allow us to create coroutines",
            "Remember always define a coroutine scope",
            "Programming is a fun and (sometimes) stressful work",
            "Today is a great day!"
        )

        fun refreshTitleWithCallbacks(callback: FetchMessageCallback){
            if(Random.nextInt(0, 11) >= 5){
                setMessage()
                callback.onCompleted()
            }
            else callback.onError(Exception("Fake Network Connection Lost"))
        }

        /**
         * refreshTitleWithCoroutines() is main-safe using coroutines
         *
         * The keyword suspend is Kotlin's way of marking a function, or function type, available to
         * coroutines. When a coroutine calls a function marked suspend, instead of blocking until that
         * function returns like a normal function call, it suspends execution until the result is ready
         * then it resumes where it left off with the result. While it's suspended waiting for a result,
         * it unblocks the thread that it's running on so other functions or coroutines can run.
         */
        suspend fun refreshTitleWithCoroutines(){
            delay(2_000)
            if(Random.nextInt(0, 11) >= 5){
                setMessage()
            }
            else throw Exception("Fake Network Connection Lost")
        }

        private fun setMessage(){
            message = messages[Random.nextInt(0, messages.size)]
        }

    }

}