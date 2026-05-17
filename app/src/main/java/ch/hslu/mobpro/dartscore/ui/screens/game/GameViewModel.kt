package ch.hslu.mobpro.dartscore.ui.screens.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity
import ch.hslu.mobpro.dartscore.data.repository.GameRepository
import ch.hslu.mobpro.dartscore.data.repository.PlayerRepository
import ch.hslu.mobpro.dartscore.data.repository.RoundRepository
import ch.hslu.mobpro.dartscore.data.round.RoundEntity
import ch.hslu.mobpro.dartscore.ui.screens.game.GameRules.DARTS_PER_ROUND
import ch.hslu.mobpro.dartscore.ui.screens.game.GameRules.isBust
import ch.hslu.mobpro.dartscore.ui.screens.game.GameRules.padWithZeroes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val roundRepository: RoundRepository
) : ViewModel() {

    private val gameId: Int = checkNotNull(savedStateHandle["gameId"])

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        loadPlayers()
    }

    fun addScore(score: Int, isDouble: Boolean) {
        val state = _uiState.value
        if (state.isGameFinished || state.isSavingTurn) return

        val currentPlayer = state.currentPlayer ?: return
        if (state.currentDarts.size >= DARTS_PER_ROUND) return

        val dart = ScoredDart(score = score, isDouble = isDouble)
        val scoreBeforeDart = currentPlayer.score - state.currentRoundScore
        val scoreAfterDart = scoreBeforeDart - score

        if (isBust(scoreAfterDart, dart)) {
            saveRoundAndMoveToNextPlayer(currentPlayer, state.currentDarts)
            return
        }

        val updatedDarts = state.currentDarts + dart
        _uiState.update {
            it.copy(currentDarts = updatedDarts)
        }

        if (updatedDarts.size == DARTS_PER_ROUND || scoreAfterDart == 0) {
            saveRoundAndMoveToNextPlayer(currentPlayer, updatedDarts)
        }
    }

    fun deleteLastDart() {
        _uiState.update { state ->
            if (state.currentDarts.isEmpty() || state.isSavingTurn) {
                state
            } else {
                state.copy(currentDarts = state.currentDarts.dropLast(1))
            }
        }
    }

    fun clearCurrentDarts() {
        _uiState.update { state ->
            if (state.isSavingTurn) {
                state
            } else {
                state.copy(currentDarts = emptyList())
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    private fun loadPlayers() {
        viewModelScope.launch {
            try {
                val game = gameRepository.getGameById(gameId)
                val players = playerRepository.getPlayersByGameId(gameId)
                val winner = players.firstOrNull { it.id == game?.winner_player_id }
                val currentPlayerIndex = if (winner != null) {
                    players.indexOfFirst { it.id == winner.id }.coerceAtLeast(0)
                } else {
                    0
                }

                _uiState.update {
                    it.copy(
                        gameType = game?.type ?: it.gameType,
                        players = players,
                        currentPlayerIndex = currentPlayerIndex,
                        currentDarts = emptyList(),
                        isLoading = false,
                        isGameFinished = game?.status == "finished",
                        winnerName = winner?.name,
                        errorMessage = if (players.isEmpty()) "No players found for this game" else null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Could not load game"
                    )
                }
            }
        }
    }

    private fun saveRoundAndMoveToNextPlayer(player: PlayerEntity, darts: List<ScoredDart>) {
        _uiState.update {
            it.copy(isSavingTurn = true)
        }

        viewModelScope.launch {
            try {
                val roundNumber = roundRepository
                    .getRoundsByPlayerAndGameId(player.id, gameId)
                    .size + 1
                val newScore = (player.score - darts.sumOf { it.score }).coerceAtLeast(0)
                val roundDarts = darts.map { it.score }.padWithZeroes()

                roundRepository.insertRound(
                    RoundEntity(
                        game_id = gameId,
                        player_id = player.id,
                        round_number = roundNumber,
                        dart1 = roundDarts[0],
                        dart2 = roundDarts[1],
                        dart3 = roundDarts[2]
                    )
                )
                playerRepository.updatePlayerScore(player.id, newScore)
                if (newScore == 0) {
                    gameRepository.finishGame(
                        gameId = gameId,
                        winnerPlayerId = player.id
                    )
                }

                _uiState.update { state ->
                    val updatedPlayers = state.players.map {
                        if (it.id == player.id) it.copy(score = newScore) else it
                    }

                    state.copy(
                        players = updatedPlayers,
                        currentPlayerIndex = if (newScore == 0) {
                            state.currentPlayerIndex
                        } else {
                            (state.currentPlayerIndex + 1) % updatedPlayers.size
                        },
                        isGameFinished = newScore == 0,
                        winnerName = if (newScore == 0) player.name else state.winnerName,
                        isSavingTurn = false,
                        currentDarts = emptyList()
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSavingTurn = false,
                        errorMessage = e.message ?: "Could not save round"
                    )
                }
            }
        }
    }

}

