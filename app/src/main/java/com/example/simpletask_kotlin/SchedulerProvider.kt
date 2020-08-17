package com.example.simpletask_kotlin

import io.reactivex.rxjava3.core.Scheduler


interface SchedulerProvider {
    fun io(): Scheduler?
    fun ui(): Scheduler?

}