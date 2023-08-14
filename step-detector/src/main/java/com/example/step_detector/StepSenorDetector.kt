package com.example.step_detector

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.core.app.ActivityCompat

/**
 * Receives sensor updates and alerts a StepListener when a step has been detected.
 */
class StepSenorDetector constructor(
    private val context: Context,
) : StepDetector, SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private var stepListener: StepListener? = null

    override fun registerListener(stepListener: StepListener): Boolean {
        this.stepListener = stepListener

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val hasPermission = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION,
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                return false
            }
        }

        val stepDetectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (stepDetectorSensor != null) {
            return sensorManager?.registerListener(
                /* listener = */ this@StepSenorDetector,
                /* sensor = */ stepDetectorSensor,
                /* samplingPeriodUs = */ SensorManager.SENSOR_DELAY_FASTEST,
            ) ?: false
        }

        return false
    }

    override fun unregisterListener() {
        stepListener = null
        sensorManager?.unregisterListener(this@StepSenorDetector)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            stepListener?.onStep(count = event.values.size)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}