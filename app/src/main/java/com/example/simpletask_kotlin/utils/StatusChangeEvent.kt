package com.example.simpletask_kotlin.utils

import org.jetbrains.annotations.TestOnly

class StatusChangeEvent(private val content: Int) {
    var hasBeenHandled = false
        @TestOnly set

    fun getContentIfNotHandled(): Int? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            return content
        }
    }

    fun peekContent() = content
}
