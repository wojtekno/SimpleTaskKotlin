package com.example.simpletask_kotlin.domain

import com.example.simpletask_kotlin.data.Status
import io.reactivex.rxjava3.core.Single

class ChangeTaskStateUseCaseImpl : ChangeTaskStateUseCase {
    override fun onTaskClicked(listId: Int): Single<Status> {
        return when (listId) {
            0 -> Single.just(Status.TRAVELLING)
            1 -> Single.just(Status.AVAILABLE)
            2 -> Single.just(Status.WORKING)
            else -> Single.error(Throwable("cannot work on two items at once"))
        }
    }

}