package ch.hslu.mobpro.dartscore.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.mobpro.dartscore.data.game.GameEntity
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity
import ch.hslu.mobpro.dartscore.data.repository.GameRepository
import ch.hslu.mobpro.dartscore.data.repository.PlayerRepository
import ch.hslu.mobpro.dartscore.ui.screens.game.GameRules
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {



    fun startGame(
        selectedMode: String,
        playerNames: List<String>,
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        val error = PlayerValidator.validatePlayerNames(playerNames)
        if (error != null) {
            onError(error)
            return
        }

        val cleanedNames = playerNames.map { it.trim() }
        val initialScore = GameRules.initialScore(selectedMode)

        viewModelScope.launch {
            try {
                gameRepository.deleteGamesInProgress()
                val game = GameEntity(
                    date = System.currentTimeMillis(),
                    type = selectedMode,
                    status = "started"
                )

                val gameId = gameRepository.insertGame(game).toInt()

                val players = cleanedNames.map { name ->
                    PlayerEntity(
                        name = name,
                        score = initialScore,
                        game_id = gameId
                    )
                }

                playerRepository.insertPlayers(players)
                onSuccess(gameId)

            } catch (e: Exception) {
                onError(e.message ?: "Could not start game")
            }
        }
    }


}


