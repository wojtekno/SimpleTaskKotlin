package com.example.simpletask_kotlin

import android.app.Application
import com.example.simpletask_kotlin.di.AppGraph
import com.example.simpletask_kotlin.log.MDebugTree
import com.github.ajalt.timberkt.Timber

class MyApplication : Application() {
    private lateinit var _appGraph: AppGraph
    val appGraph get() = _appGraph

    override fun onCreate() {
        super.onCreate()
        Timber.plant(MDebugTree())
        _appGraph = AppGraph()


    }


}