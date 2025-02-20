package com.example.jeudushifumi.model

data class GameState(
    val playerChoice: GameChoice? = null,
    val computerChoice: GameChoice? = null,
    val result: GameResult? = null,
    val playerScore: Int = 0,
    val computerScore: Int = 0
)