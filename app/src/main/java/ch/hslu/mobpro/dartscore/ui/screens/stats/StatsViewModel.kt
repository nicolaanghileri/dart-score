package ch.hslu.mobpro.dartscore.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.mobpro.dartscore.data.repository.GameRepository
import ch.hslu.mobpro.dartscore.data.repository.PlayerRepository
import ch.hslu.mobpro.dartscore.data.repository.RoundRepository
import ch.hslu.mobpro.dartscore.domain.stats.GameSummaryUiModel
import ch.hslu.mobpro.dartscore.domain.stats.StatsCalculator
import ch.hslu.mobpro.dartscore.domain.stats.StatsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val roundRepository: RoundRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val summaries = gameRepository
                    .getAllGames()
                    .sortedByDescending { it.date }
                    .map { game ->
                        val players = playerRepository.getPlayersByGameId(game.id)
                        val rounds = roundRepository.getRoundsByGameId(game.id)
                        val roundScores = rounds.map { it.dart1 + it.dart2 + it.dart3 }
                        val winner = players.firstOrNull { it.id == game.winner_player_id }

                        GameSummaryUiModel(
                            id = game.id,
                            date = dateFormatter.format(Date(game.date)),
                            playersText = players.joinToString(" vs ") { it.name },
                            winnerText = winner?.name
                                ?: if (game.status == "finished") "Unknown" else "In progress",
                            throws = StatsCalculator.throwCount(rounds.size),
                            avgScore = StatsCalculator.averageScore(roundScores),
                            highest = StatsCalculator.highestScore(roundScores)
                        )
                    }

                _uiState.update {
                    it.copy(
                        summaries = summaries,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Could not load stats"
                    )
                }
            }
        }
    }

    private companion object {
        val dateFormatter = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    }
}

