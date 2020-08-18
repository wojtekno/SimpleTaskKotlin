package com.example.simpletask_kotlin

import android.app.Application
import com.example.simpletask_kotlin.di.AppGraph

class MyApplication : Application() {
    private lateinit var _appGraph : AppGraph
    val appGraph get() = _appGraph

    override fun onCreate() {
        super.onCreate()
        _appGraph = AppGraph()
    }


}