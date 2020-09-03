package com.example.simpletask_kotlin.utils

import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class StatusChangeEventTest {

    @Test
    fun `given StatusChangeEvent initialized when peekContent() then return content`() {
        val mockContent = 13
        val sce = StatusChangeEvent(mockContent)
        val content = sce.peekContent()
        expectThat(content).isEqualTo(mockContent)
    }

    @Test
    fun `given StatusChangeEvent initialized when getContentIfNotHandled then return content`() {
        val mockContent = 13
        val sce = StatusChangeEvent(mockContent)
        val content = sce.getContentIfNotHandled()
        expectThat(content).isEqualTo(mockContent)
    }

    @Test
    fun `given StatusChangeEvent initialized when getContentIfNotHandled then hasBeenHandled is true`() {
        val mockContent = 13
        val sce = StatusChangeEvent(mockContent)
        val content = sce.getContentIfNotHandled()
        expectThat(sce.hasBeenHandled).isEqualTo(true)
    }

    @Test
    fun `given hasBeenHandled is true when getContentIfNotHandled then return null`() {
        val mockContent = 13
        val sce = StatusChangeEvent(mockContent).apply { hasBeenHandled = true }
        val content = sce.getContentIfNotHandled()
        expectThat(content).isEqualTo(null)
    }

    @Test
    fun `given hasBeenHandled is true when peekContent() then return content`() {
        val mockContent = 13
        val sce = StatusChangeEvent(mockContent).apply { hasBeenHandled = true }
        val content = sce.peekContent()
        expectThat(content).isEqualTo(mockContent)
    }
}