package ch.hslu.mobpro.dartscore.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ch.hslu.mobpro.dartscore.data.game.GameEntity
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity
import ch.hslu.mobpro.dartscore.data.repository.GameRepository
import ch.hslu.mobpro.dartscore.data.repository.PlayerRepository
import kotlinx.coroutines.launch

class HomeViewModel (
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {



    fun startGame(
        selectedMode: String,
        playerNames: List<String>,
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        val cleanedNames = playerNames.map { it.trim() }
        if (cleanedNames.any { it.isBlank() }) {
            onError("Player name cannot be empty")
            return
        }

        if (cleanedNames.any {  it.length < 3 }) {
            onError("Player names must have at least 3 characters")
            return
        }
        if (cleanedNames.size < 2) {
            onError("At least 2 players are required")
            return
        }

        val hasDuplicates = cleanedNames
            .map { it.lowercase() }
            .distinct()
            .size != cleanedNames.size
        if (hasDuplicates) {
            onError("Player names must be unique")
            return
        }

        viewModelScope.launch {
            try {
                val game = GameEntity(
                    date = System.currentTimeMillis(),
                    type = selectedMode,
                    status = "started"
                )

                val gameId = gameRepository.insertGame(game).toInt()

                val initialScore = getInitialScore(selectedMode)

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

    private fun getInitialScore(selectedMode: String): Int {
        return when (selectedMode) {
            "301" -> 301
            "501" -> 501
            "701" -> 701
            "Cricket" -> 0
            else -> 501
        }
    }


}


class HomeViewModelFactory(
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(gameRepository, playerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}