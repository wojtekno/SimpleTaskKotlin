package com.example.simpletask_kotlin.domain

import com.example.simpletask_kotlin.data.Status
import io.reactivex.rxjava3.core.Single

interface ChangeTaskStateUseCase {
    fun onTaskClicked(listId: Int): Single<Status>

}