package com.example.step_detector

interface StepDetector {

    fun registerListener(stepListener: StepListener): Boolean

    fun unregisterListener()
}