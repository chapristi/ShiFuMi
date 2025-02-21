package com.example.jeudushifumi.utils

import com.example.jeudushifumi.R
import com.example.jeudushifumi.model.GameChoice

fun getChoiceImage(choice: GameChoice): Int {
    return when (choice) {
        GameChoice.ROCK -> R.drawable.rock
        GameChoice.PAPER -> R.drawable.paper
        GameChoice.SCISSORS -> R.drawable.scissors
    }
}
