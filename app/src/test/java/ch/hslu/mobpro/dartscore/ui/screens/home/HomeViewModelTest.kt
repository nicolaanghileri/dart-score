package ch.hslu.mobpro.dartscore.ui.screens.home

import ch.hslu.mobpro.dartscore.data.repository.GameRepository
import ch.hslu.mobpro.dartscore.data.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @Test
    fun testStartGame() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        var successGameId: Int? = null
        var errorMessage: String? = null

        val gameRepository = mock<GameRepository>()
        val playerRepository = mock<PlayerRepository>()

        whenever(gameRepository.insertGame(any())).thenReturn(1L)

        val viewModel = HomeViewModel(
            gameRepository = gameRepository,
            playerRepository = playerRepository
        )

        viewModel.startGame(
            selectedMode = "501",
            playerNames = listOf("Thomas", "Nicola"),
            onSuccess = { gameId -> successGameId = gameId },
            onError = { message -> errorMessage = message }
        )

        verify(gameRepository).insertGame(any())
        verify(playerRepository).insertPlayers(any())

        assertEquals(1, successGameId)
        assertNull(errorMessage)

        Dispatchers.resetMain()
    }

    @Test
    fun testStartGameDeletesStartedGamesBeforeCreatingNewGame() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        val gameRepository = mock<GameRepository>()
        val playerRepository = mock<PlayerRepository>()

        whenever(gameRepository.insertGame(any())).thenReturn(1L)

        val viewModel = HomeViewModel(
            gameRepository = gameRepository,
            playerRepository = playerRepository
        )

        viewModel.startGame(
            selectedMode = "501",
            playerNames = listOf("Thomas", "Alex"),
            onSuccess = {},
            onError = {}
        )

        val inOrder = inOrder(gameRepository, playerRepository)

        inOrder.verify(gameRepository).deleteGamesInProgress()
        inOrder.verify(gameRepository).insertGame(any())
        inOrder.verify(playerRepository).insertPlayers(any())
    }

}