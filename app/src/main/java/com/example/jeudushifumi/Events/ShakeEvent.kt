package com.example.jeudushifumi.Events

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeEvent(private val onShake: () -> Unit) : SensorEventListener {
    private var lastShakeTime: Long = 0

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.values.size < 3) return

            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]
            val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

            val currentTime = System.currentTimeMillis()
            if (acceleration > SHAKE_THRESHOLD && currentTime - lastShakeTime > SHAKE_TIME_LAPSE) {
                lastShakeTime = currentTime
                onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Pas d'implémentation nécessaire ici
    }

    fun resetShakeTime() {
        lastShakeTime = 0
    }

    companion object {
        private const val SHAKE_THRESHOLD = 3.0f
        private const val SHAKE_TIME_LAPSE = 1300
    }
}
