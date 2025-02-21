package com.example.jeudushifumi.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.jeudushifumi.R
import com.example.jeudushifumi.model.GameChoice
import com.example.jeudushifumi.model.GameResult
import com.example.jeudushifumi.model.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {
    val shakeText = mutableStateOf("Secoue pour jouer")
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun onShakeDetected() {
        val playerChoice = generateComputerChoice()
        val computerChoice = generateComputerChoice()
        val result = determineResult(playerChoice, computerChoice)

        val currentPlayerScore = _gameState.value.playerScore
        val currentComputerScore = _gameState.value.computerScore

        val newPlayerScore = if (result == GameResult.WIN) currentPlayerScore + 1 else currentPlayerScore
        val newComputerScore = if (result == GameResult.LOSE) currentComputerScore + 1 else currentComputerScore

        _gameState.value = GameState(
            playerChoice = playerChoice,
            computerChoice = computerChoice,
            result = result,
            playerScore = newPlayerScore,
            computerScore = newComputerScore
        )
    }



    private fun generateComputerChoice(): GameChoice {
        return GameChoice.values().random()
    }

    private fun determineResult(playerChoice: GameChoice, computerChoice: GameChoice): GameResult {
        return when {
            playerChoice == computerChoice -> GameResult.DRAW
            (playerChoice == GameChoice.ROCK && computerChoice == GameChoice.SCISSORS) ||
                    (playerChoice == GameChoice.PAPER && computerChoice == GameChoice.ROCK) ||
                    (playerChoice == GameChoice.SCISSORS && computerChoice == GameChoice.PAPER) -> GameResult.WIN
            else -> GameResult.LOSE
        }
    }

    fun getChoiceImage(choice: GameChoice): Int {
        return when (choice) {
            GameChoice.ROCK -> R.drawable.rock
            GameChoice.PAPER -> R.drawable.paper
            GameChoice.SCISSORS -> R.drawable.scissors
        }
    }

    fun resetGame() {
        _gameState.value = GameState()
    }
}
