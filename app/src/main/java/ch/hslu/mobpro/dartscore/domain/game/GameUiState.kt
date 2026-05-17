package ch.hslu.mobpro.dartscore.domain.game

import ch.hslu.mobpro.dartscore.data.player.PlayerEntity

data class GameUiState(
    val gameType: String = "501",
    val players: List<PlayerEntity> = emptyList(),
    val currentPlayerIndex: Int = 0,
    val currentDarts: List<ScoredDart> = emptyList(),
    val isLoading: Boolean = true,
    val isSavingTurn: Boolean = false,
    val isGameFinished: Boolean = false,
    val winnerName: String? = null,
    val errorMessage: String? = null
) {
    val currentPlayer: PlayerEntity?
        get() = players.getOrNull(currentPlayerIndex)

    val currentRoundScore: Int
        get() = currentDarts.sumOf { it.score }

    val pointsRemaining: Int
        get() = (currentPlayer?.score ?: GameRules.initialScore(gameType)) - currentRoundScore
}