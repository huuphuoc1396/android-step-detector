package com.example.stepdetector

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.step_detector.AccelSensorDetector
import com.example.step_detector.StepListener
import com.example.step_detector.StepSenorDetector
import com.example.stepdetector.databinding.ActivityMainBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.models.PermissionRequest

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private var viewBinding: ActivityMainBinding? = null
    private var accelSteps = 0
    private var stepDetectorSteps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        requestActivityRecognitionIfNeeded()
        resetSteps()
        buildResetAction()
    }

    private fun setViewBinding() {
        viewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding!!.root)
    }

    private fun requestActivityRecognitionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val request = PermissionRequest.Builder(this)
                .perms(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)).build()
            EasyPermissions.requestPermissions(this, request)
        } else {
            buildStepDetector()
        }
    }

    private fun buildResetAction() {
        viewBinding?.buttonResetSteps?.setOnClickListener {
            resetSteps()
        }
    }

    private fun resetSteps() {
        accelSteps = 0
        stepDetectorSteps = 0
        viewBinding?.apply {
            textAccelSensor.text = getString(R.string.text_steps_using_accel_sensor, 0)
            textStepSenor.text = getString(R.string.text_steps_using_step_detector_sensor, 0)
        }
    }

    private fun buildStepDetector() {
        val accelSensorDetector = AccelSensorDetector(this@MainActivity)
        val stepSensorDetector = StepSenorDetector(this@MainActivity)

        val availableAccel = accelSensorDetector.registerListener(object : StepListener {
            override fun onStep(count: Int) {
                accelSteps += count
                viewBinding?.textAccelSensor?.text =
                    getString(R.string.text_steps_using_accel_sensor, accelSteps)
            }
        })

        val availableStepDetector = stepSensorDetector.registerListener(object : StepListener {
            override fun onStep(count: Int) {
                stepDetectorSteps += count
                viewBinding?.textStepSenor?.text =
                    getString(R.string.text_steps_using_step_detector_sensor, stepDetectorSteps)
            }
        })

        var error = ""
        if (!availableAccel) {
            error += getText(R.string.text_error_unavailable_accel_sensor)
        }
        if (!availableStepDetector) {
            if (error.isNotEmpty()) {
                error += "\n\n"
            }
            error += getText(R.string.text_error_unavailable_step_detector_sensor)
        }
        viewBinding?.textError?.text = error
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode = requestCode, permissions = permissions, grantResults = grantResults, this
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        buildStepDetector()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        buildStepDetector()
    }

    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }
}