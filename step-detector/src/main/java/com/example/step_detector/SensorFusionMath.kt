/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.example.step_detector

import kotlin.math.sqrt

/**
 * A collection of matrix and vector operations used specifically for sensor
 * fusion.  These are purposefully specific so as to be fast.
 */
object SensorFusionMath {
    @JvmStatic
    fun sum(array: FloatArray): Float {
        var retval = 0f
        for (i in array.indices) {
            retval += array[i]
        }
        return retval
    }

    @JvmStatic
    fun cross(arrayA: FloatArray, arrayB: FloatArray): FloatArray {
        val retArray = FloatArray(3)
        retArray[0] = arrayA[1] * arrayB[2] - arrayA[2] * arrayB[1]
        retArray[1] = arrayA[2] * arrayB[0] - arrayA[0] * arrayB[2]
        retArray[2] = arrayA[0] * arrayB[1] - arrayA[1] * arrayB[0]
        return retArray
    }

    @JvmStatic
    fun norm(array: FloatArray): Float {
        var retval = 0f
        for (i in array.indices) {
            retval += array[i] * array[i]
        }
        return sqrt(retval.toDouble()).toFloat()
    }

    // Note: only works with 3D vectors.
    @JvmStatic
    fun dot(a: FloatArray, b: FloatArray): Float {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2]
    }

    @JvmStatic
    fun normalize(a: FloatArray): FloatArray {
        val retval = FloatArray(a.size)
        val norm = norm(a)
        for (i in a.indices) {
            retval[i] = a[i] / norm
        }
        return retval
    }
}