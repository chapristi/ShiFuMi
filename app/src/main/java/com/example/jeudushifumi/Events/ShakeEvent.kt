package com.example.jeudushifumi.Events

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeEvent(private val onShake: () -> Unit) : SensorEventListener {
    private var lastShakeTime: Long = 0
    private var shakeCount = 0

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.values.size < 3) return

            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

            val currentTime = System.currentTimeMillis()

            if (acceleration > SHAKE_THRESHOLD) {
                if (currentTime - lastShakeTime < DEBOUNCE_TIME) {
                    return
                }

                if (currentTime - lastShakeTime > SHAKE_TIME_LAPSE) {
                    shakeCount = 0
                }

                lastShakeTime = currentTime
                shakeCount++

                if (shakeCount >= REQUIRED_SHAKES) {
                    resetShakeTime()
                    onShake()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun resetShakeTime() {
        lastShakeTime = System.currentTimeMillis() + COOLDOWN_TIME
        shakeCount = 0
    }

    companion object {
        private const val SHAKE_THRESHOLD = 12.0f
        private const val SHAKE_TIME_LAPSE = 500
        private const val REQUIRED_SHAKES = 3
        private const val DEBOUNCE_TIME = 300
        private const val COOLDOWN_TIME = 1000
    }
}
