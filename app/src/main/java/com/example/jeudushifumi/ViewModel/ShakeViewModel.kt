package com.example.jeudushifumi.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ShakeViewModel : ViewModel() {
    val shakeText = mutableStateOf("Page de jeux")

    fun onShakeDetected() {
        shakeText.value = "Secousse détectée!"
    }
}
