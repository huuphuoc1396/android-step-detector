package com.example.step_detector

import android.content.Context

class StepDetectorImpl constructor(
    context: Context,
) : StepDetector {

    private val accelSensorDetector = AccelSensorDetector(context)
    private val stepSensorDetector = StepSenorDetector(context)

    override fun registerListener(stepListener: StepListener): Boolean {
        val stepSenorAvailability = stepSensorDetector.registerListener(stepListener)
        if (!stepSenorAvailability) {
            val accelSensorAvailability = accelSensorDetector.registerListener(stepListener)
            if (!accelSensorAvailability) {
                return false
            }
        }
        return true
    }

    override fun unregisterListener() {
        accelSensorDetector.unregisterListener()
        stepSensorDetector.unregisterListener()
    }
}