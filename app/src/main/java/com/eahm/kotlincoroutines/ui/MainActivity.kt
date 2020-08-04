package com.eahm.kotlincoroutines.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eahm.kotlincoroutines.R
import com.eahm.kotlincoroutines.adapters.MenuAdapter
import com.eahm.kotlincoroutines.models.MenuElement
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val menuElements = listOf(
        MenuElement(
            "Threads with suspend fun",
            "Check how powerful coroutines are when we create many number of task",
            ThreadsWithSuspendFun::class.java
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMainMenu.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = MenuAdapter(menuElements)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL) )
        }

    }

}