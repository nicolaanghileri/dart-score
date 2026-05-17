package ch.hslu.mobpro.dartscore.ui.screens.stats

import ch.hslu.mobpro.dartscore.data.game.GameEntity
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity
import ch.hslu.mobpro.dartscore.data.repository.GameRepository
import ch.hslu.mobpro.dartscore.data.repository.PlayerRepository
import ch.hslu.mobpro.dartscore.data.repository.RoundRepository
import ch.hslu.mobpro.dartscore.data.round.RoundEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class StatsViewModelTest {

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
    fun testLoadStatsSuccess() = runTest {
        whenever(gameRepository.getAllGames()).thenReturn(
            listOf(
                GameEntity(
                    id = 1,
                    date = 1_700_000_000_000L,
                    type = "501",
                    status = "finished",
                    winner_player_id = 1
                )
            )
        )

        whenever(playerRepository.getPlayersByGameId(1)).thenReturn(
            listOf(
                PlayerEntity(id = 1, name = "Thomas", score = 0, game_id = 1),
                PlayerEntity(id = 2, name = "Alex", score = 120, game_id = 1)
            )
        )

        whenever(roundRepository.getRoundsByGameId(1)).thenReturn(
            listOf(
                RoundEntity(
                    game_id = 1,
                    player_id = 1,
                    round_number = 1,
                    dart1 = 20,
                    dart2 = 20,
                    dart3 = 20
                ),
                RoundEntity(
                    game_id = 1,
                    player_id = 2,
                    round_number = 1,
                    dart1 = 60,
                    dart2 = 40,
                    dart3 = 0
                )
            )
        )

        val viewModel = createViewModel()

        val state = viewModel.uiState.value
        val summary = state.summaries.first()

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(1, state.summaries.size)

        assertEquals(1, summary.id)
        assertEquals("Thomas vs Alex", summary.playersText)
        assertEquals("Thomas", summary.winnerText)
        assertEquals(6, summary.throws)
        assertEquals(80, summary.avgScore)
        assertEquals(100, summary.highest)
    }

    @Test
    fun testLoadStatsSortsGamesDescendingByDate() = runTest {
        whenever(gameRepository.getAllGames()).thenReturn(
            listOf(
                GameEntity(id = 1, date = 1000L, type = "501", status = "started"),
                GameEntity(id = 2, date = 3000L, type = "501", status = "started"),
                GameEntity(id = 3, date = 2000L, type = "501", status = "started")
            )
        )

        whenever(playerRepository.getPlayersByGameId(any())).thenReturn(emptyList())
        whenever(roundRepository.getRoundsByGameId(any())).thenReturn(emptyList())

        val viewModel = createViewModel()

        val summaries = viewModel.uiState.value.summaries

        assertEquals(listOf(2, 3, 1), summaries.map { it.id })
    }

    @Test
    fun testLoadStatsShowsInProgressWhenGameNotFinished() = runTest {
        whenever(gameRepository.getAllGames()).thenReturn(
            listOf(
                GameEntity(
                    id = 1,
                    date = 1000L,
                    type = "501",
                    status = "started",
                    winner_player_id = null
                )
            )
        )

        whenever(playerRepository.getPlayersByGameId(1)).thenReturn(
            listOf(PlayerEntity(id = 1, name = "Thomas", score = 501, game_id = 1))
        )

        whenever(roundRepository.getRoundsByGameId(1)).thenReturn(emptyList())

        val viewModel = createViewModel()

        val summary = viewModel.uiState.value.summaries.first()

        assertEquals("In progress", summary.winnerText)
    }

    @Test
    fun testLoadStatsShowsUnknownWhenFinishedWithoutWinner() = runTest {
        whenever(gameRepository.getAllGames()).thenReturn(
            listOf(
                GameEntity(
                    id = 1,
                    date = 1000L,
                    type = "501",
                    status = "finished",
                    winner_player_id = null
                )
            )
        )

        whenever(playerRepository.getPlayersByGameId(1)).thenReturn(
            listOf(PlayerEntity(id = 1, name = "Thomas", score = 0, game_id = 1))
        )

        whenever(roundRepository.getRoundsByGameId(1)).thenReturn(emptyList())

        val viewModel = createViewModel()

        val summary = viewModel.uiState.value.summaries.first()

        assertEquals("Unknown", summary.winnerText)
    }

    @Test
    fun testLoadStatsEmptyGames() = runTest {
        whenever(gameRepository.getAllGames()).thenReturn(emptyList())

        val viewModel = createViewModel()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertTrue(state.summaries.isEmpty())
    }

    @Test
    fun testLoadStatsError() = runTest {
        whenever(gameRepository.getAllGames()).thenThrow(RuntimeException("Load failed"))

        val viewModel = createViewModel()

        val state = viewModel.uiState.value

        assertFalse(state.isLoading)
        assertEquals("Load failed", state.errorMessage)
        assertTrue(state.summaries.isEmpty())
    }

    private fun createViewModel(): StatsViewModel {
        return StatsViewModel(
            gameRepository = gameRepository,
            playerRepository = playerRepository,
            roundRepository = roundRepository
        )
    }
}