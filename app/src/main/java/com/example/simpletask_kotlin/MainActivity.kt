package com.example.simpletask_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simpletask_kotlin.ui.tasks.TasksFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, TasksFragment.newInstance())
                    .commitNow()
        }
    }
}