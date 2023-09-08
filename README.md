# Android Step Detector

This example uses either **`TYPE_STEP_DETECTOR`** or **`TYPE_ACCELEROMETER`** sensors to detect a step trigger. In my own case, I don't use **`TYPE_STEP_COUNTER`**, because there are some Xiaomi devices, where this sensor isn't triggered immediately. If you want to continuously receive a steps trigger on as many devices as possible, `TYPE_STEP_COUNTER` isn't recommended. Also, there are some devices that don't have `TYPE_STEP_DETECTOR` or it's unavailable. Therefore, there is a replacement solution that uses `TYPE_ACCELEROMETER`. It will self-calculate steps using Sensor Fusion Math. Feel free to check more in this repository [simple-pedometer](https://github.com/google/simple-pedometer)

<img src="https://github.com/huuphuoc1396/AndroidStepDetector/raw/main/app-screenshot.png" width="300">


## Usages
- You could choose which sensor you would like to use in your case.
```Kotlin
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
```

- Or you could use `StepDetectorFactory.create(Context)`. It will check TYPE_STEP_DETECTOR first, if it's unavailable, it will check to TYPE_ACCELEROMETER. Both sensors are unavailable, `stepDetector.registerListener(StepListener)` will return `false`
```Kotlin
    private fun buildStepDetector() {
        val stepDetector = StepDetectorFactory.create(this)

        val isAvailable = stepDetector.registerListener(object : StepListener {
            override fun onStep(count: Int) {
                accelSteps += count
                viewBinding?.textAccelSensor?.text = getString(R.string.text_steps, accelSteps)
            }
        })
        
        if (!isAvailable) {
            viewBinding?.textError?.text = getText(R.string.text_error_unavailable_sensor)
        }
    }
```

## Demo
You can check demo APK [here](https://github.com/huuphuoc1396/AndroidStepDetector/blob/main/app-debug.apk)

## References
- [simple-pedometer](https://github.com/google/simple-pedometer)

If you find my assistance helpful, feel free to support me by [buying me a coffee](https://www.buymeacoffee.com/huuphuoc1396) or following me on [GitHub](https://github.com/huuphuoc1396). Thank you!!!

<a href="https://www.buymeacoffee.com/huuphuoc1396" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>
