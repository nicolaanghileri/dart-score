package ch.hslu.mobpro.dartscore.ui.screens.game

import androidx.lifecycle.SavedStateHandle
import ch.hslu.mobpro.dartscore.data.game.GameEntity
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity
import ch.hslu.mobpro.dartscore.data.repository.GameRepository
import ch.hslu.mobpro.dartscore.data.repository.PlayerRepository
import ch.hslu.mobpro.dartscore.data.repository.RoundRepository
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var gameRepository: GameRepository
    private lateinit var playerRepository: PlayerRepository
    private lateinit var roundRepository: RoundRepository

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        gameRepository = mock()
        playerRepository = mock()
        roundRepository = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadPlayersSuccess() = runTest {
        whenever(gameRepository.getGameById(1)).thenReturn(
            GameEntity(
                id = 1,
                date = 1L,
                type = "501",
                status = "started"
            )
        )

        whenever(playerRepository.getPlayersByGameId(1)).thenReturn(
            listOf(
                PlayerEntity(id = 1, name = "Thomas", score = 501, game_id = 1),
                PlayerEntity(id = 2, name = "Nicola", score = 501, game_id = 1)
            )
        )

        val viewModel = createViewModel()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals("501", state.gameType)
        assertEquals(2, state.players.size)
        assertEquals("Thomas", state.currentPlayer?.name)
        assertNull(state.errorMessage)
    }

    @Test
    fun testLoadPlayersEmptyShowsError() = runTest {
        whenever(gameRepository.getGameById(1)).thenReturn(
            GameEntity(
                id = 1,
                date = 1L,
                type = "501",
                status = "started"
            )
        )

        whenever(playerRepository.getPlayersByGameId(1)).thenReturn(emptyList())

        val viewModel = createViewModel()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals("No players found for this game", state.errorMessage)
    }

    @Test
    fun testAddScoreAddsDart() = runTest {
        prepareStartedGame()

        val viewModel = createViewModel()

        viewModel.addScore(score = 20, isDouble = false)

        val state = viewModel.uiState.value

        assertEquals(1, state.currentDarts.size)
        assertEquals(20, state.currentRoundScore)
        assertEquals(481, state.pointsRemaining)
    }

    @Test
    fun testAddScoreAfterThreeDartsSavesRoundAndMovesToNextPlayer() = runTest {
        prepareStartedGame()
        whenever(roundRepository.getRoundsByPlayerAndGameId(1, 1)).thenReturn(emptyList())

        val viewModel = createViewModel()

        viewModel.addScore(score = 20, isDouble = false)
        viewModel.addScore(score = 20, isDouble = false)
        viewModel.addScore(score = 20, isDouble = false)

        val state = viewModel.uiState.value

        verify(roundRepository).insertRound(any())
        verify(playerRepository).updatePlayerScore(1, 441)

        assertEquals(1, state.currentPlayerIndex)
        assertTrue(state.currentDarts.isEmpty())
        assertEquals(441, state.players.first { it.id == 1 }.score)
    }

    @Test
    fun testAddScoreBustMovesToNextPlayerWithoutSubtractingScore() = runTest {
        prepareStartedGame(
            players = listOf(
                PlayerEntity(id = 1, name = "Thomas", score = 10, game_id = 1),
                PlayerEntity(id = 2, name = "Nicola", score = 501, game_id = 1)
            )
        )
        whenever(roundRepository.getRoundsByPlayerAndGameId(1, 1)).thenReturn(emptyList())

        val viewModel = createViewModel()

        viewModel.addScore(score = 20, isDouble = false)

        val state = viewModel.uiState.value

        verify(playerRepository).updatePlayerScore(1, 10)

        assertEquals(1, state.currentPlayerIndex)
        assertEquals(10, state.players.first { it.id == 1 }.score)
        assertTrue(state.currentDarts.isEmpty())
    }

    @Test
    fun testAddScoreFinishesGameWithDouble() = runTest {
        prepareStartedGame(
            players = listOf(
                PlayerEntity(id = 1, name = "Thomas", score = 40, game_id = 1),
                PlayerEntity(id = 2, name = "Nicola", score = 501, game_id = 1)
            )
        )
        whenever(roundRepository.getRoundsByPlayerAndGameId(1, 1)).thenReturn(emptyList())

        val viewModel = createViewModel()

        viewModel.addScore(score = 40, isDouble = true)

        val state = viewModel.uiState.value

        verify(playerRepository).updatePlayerScore(1, 0)
        verify(gameRepository).finishGame(gameId = 1, winnerPlayerId = 1)

        assertTrue(state.isGameFinished)
        assertEquals("Thomas", state.winnerName)
        assertEquals(0, state.players.first { it.id == 1 }.score)
    }

    @Test
    fun testAddScoreFinishesWithoutDoubleIsBust() = runTest {
        prepareStartedGame(
            players = listOf(
                PlayerEntity(id = 1, name = "Thomas", score = 40, game_id = 1),
                PlayerEntity(id = 2, name = "Nicola", score = 501, game_id = 1)
            )
        )
        whenever(roundRepository.getRoundsByPlayerAndGameId(1, 1)).thenReturn(emptyList())

        val viewModel = createViewModel()

        viewModel.addScore(score = 40, isDouble = false)

        val state = viewModel.uiState.value

        verify(playerRepository).updatePlayerScore(1, 40)
         verify(gameRepository, never()).finishGame(any(), any())

        assertFalse(state.isGameFinished)
        assertEquals(1, state.currentPlayerIndex)
        assertEquals(40, state.players.first { it.id == 1 }.score)
    }

    @Test
    fun testDeleteLastDartRemovesLastDart() = runTest {
        prepareStartedGame()

        val viewModel = createViewModel()

        viewModel.addScore(score = 20, isDouble = false)
        viewModel.addScore(score = 5, isDouble = false)
        viewModel.deleteLastDart()

        val state = viewModel.uiState.value

        assertEquals(1, state.currentDarts.size)
        assertEquals(20, state.currentRoundScore)
    }

    @Test
    fun testClearCurrentDartsRemovesAllDarts() = runTest {
        prepareStartedGame()

        val viewModel = createViewModel()

        viewModel.addScore(score = 20, isDouble = false)
        viewModel.addScore(score = 5, isDouble = false)
        viewModel.clearCurrentDarts()

        assertTrue(viewModel.uiState.value.currentDarts.isEmpty())
    }

    @Test
    fun testClearErrorSetsErrorMessageToNull() = runTest {
        whenever(gameRepository.getGameById(1)).thenThrow(RuntimeException("Load failed"))

        val viewModel = createViewModel()

        assertEquals("Load failed", viewModel.uiState.value.errorMessage)

        viewModel.clearError()

        assertNull(viewModel.uiState.value.errorMessage)
    }

    private fun createViewModel(): GameViewModel {
        return GameViewModel(
            savedStateHandle = SavedStateHandle(mapOf("gameId" to 1)),
            gameRepository = gameRepository,
            playerRepository = playerRepository,
            roundRepository = roundRepository
        )
    }

    private suspend fun prepareStartedGame(
        players: List<PlayerEntity> = listOf(
            PlayerEntity(id = 1, name = "Thomas", score = 501, game_id = 1),
            PlayerEntity(id = 2, name = "Nicola", score = 501, game_id = 1)
        )
    ) {
        whenever(gameRepository.getGameById(1)).thenReturn(
            GameEntity(
                id = 1,
                date = 1L,
                type = "501",
                status = "started"
            )
        )

        whenever(playerRepository.getPlayersByGameId(1)).thenReturn(players)
    }
}