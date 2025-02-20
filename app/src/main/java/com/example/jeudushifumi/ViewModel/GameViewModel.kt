package com.example.jeudushifumi.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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
        var playerChoice = this.generateComputerChoice();
        var computerChoice = this.generateComputerChoice();

        shakeText.value = "Le joueur a joué " + playerChoice.toString()
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

    fun resetGame() {
        _gameState.value = GameState()
    }
}
