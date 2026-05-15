package ch.hslu.mobpro.dartscore.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import ch.hslu.mobpro.dartscore.data.DartScoreDatabase
import ch.hslu.mobpro.dartscore.data.repository.GameRepository
import ch.hslu.mobpro.dartscore.data.repository.PlayerRepository
import ch.hslu.mobpro.dartscore.ui.components.AppErrorDialog
import ch.hslu.mobpro.dartscore.ui.screens.home.components.GameModeCard
import ch.hslu.mobpro.dartscore.ui.screens.home.components.PlayerInputField
import ch.hslu.mobpro.dartscore.ui.theme.DartScoreTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val homeViewModel = getHomeViewModel()

    var selectedMode by rememberSaveable { mutableStateOf("501") }

    val players = rememberSaveable { mutableStateListOf("", "") }

    val gameModes = listOf(
        Triple(Icons.Outlined.GpsFixed, "501", "Classic 501 game"),
        Triple(Icons.Outlined.Bolt, "301", "Fast 301 game"),
        Triple(Icons.Outlined.EmojiEvents, "701", "Extended 701 game"),
        Triple(Icons.Outlined.GpsFixed, "Cricket", "Cricket scoring"),
    )

    var errorMessage by remember { mutableStateOf<String?>(null) }

    AppErrorDialog(
        message = errorMessage,
        onDismiss = {
            errorMessage = null
        }
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "New Darts Game",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        ModesCards(
            gameModes = gameModes,
            selectedMode = selectedMode,
            onModeSelected = { mode ->
                selectedMode = mode
            }
        )

        Spacer(modifier = Modifier.height(15.dp))

        PlayerInputs(
            players = players
        )

        Button(
            onClick = {
                homeViewModel.startGame(
                    selectedMode = selectedMode,
                    playerNames = players,
                    onSuccess = { gameId ->
                        navController.navigate("game/$gameId")
                    },
                    onError = { message ->
                        errorMessage = message
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(" Start Game", color = MaterialTheme.colorScheme.onSurface)
        }
        Spacer(Modifier.height(88.dp))
    }
}

@Composable
private fun ModesCards(
    modifier: Modifier = Modifier,
    gameModes: List<Triple<ImageVector, String, String>>,
    selectedMode: String,
    onModeSelected: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp),
        userScrollEnabled = false
    ) {
        items(gameModes) { (icon, title, subtitle) ->
            GameModeCard(
                icon = icon,
                title = title,
                subtitle = subtitle,
                isSelected = selectedMode == title,
                onClick = {
                    onModeSelected(title)
                }
            )
        }
    }
}

@Composable
private fun PlayerInputs(
    players: MutableList<String>,
) {
    Text("Players")

    Spacer(Modifier.height(8.dp))

    players.forEachIndexed { index, name ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerInputField(
                value = name,
                onValueChange = { players[index] = it },
                modifier = Modifier.weight(1f)
            )

            if (players.size > 2) {
                IconButton(
                    onClick = {
                        players.removeAt(index)
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove player",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
    }

    Button(
        onClick = { players.add("") },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(" Add Player", color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun getHomeViewModel(): HomeViewModel {
    val context = LocalContext.current.applicationContext
    val database = DartScoreDatabase.getDatabase(context)
    val gameRepo = GameRepository(database.gameDao())
    val playerRepo = PlayerRepository(database.playerDao())

    return viewModel(
        factory = HomeViewModelFactory(gameRepo, playerRepo)
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DartScoreTheme {
        HomeScreen(navController = NavHostController(LocalContext.current))
    }
}