package ch.hslu.mobpro.dartscore.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import ch.hslu.mobpro.dartscore.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ch.hslu.mobpro.dartscore.ui.components.AppErrorDialog
import ch.hslu.mobpro.dartscore.ui.navigation.AppScreens
import ch.hslu.mobpro.dartscore.ui.screens.home.components.GameModeCard
import ch.hslu.mobpro.dartscore.ui.screens.home.components.PlayerInputField
import ch.hslu.mobpro.dartscore.ui.theme.DartScoreTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val homeViewModel: HomeViewModel = hiltViewModel()

    var selectedMode by rememberSaveable { mutableStateOf("501") }

    val players = rememberSaveable { mutableStateListOf("", "") }

    val gameModes = listOf(
        Triple(Icons.Outlined.GpsFixed, "501", stringResource(R.string.game_mode_classic_501)),
        Triple(Icons.Outlined.Bolt, "301", stringResource(R.string.game_mode_fast_301)),
        Triple(Icons.Outlined.EmojiEvents, "701", stringResource(R.string.game_mode_extended_701))
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
            text = stringResource(R.string.new_game),
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(R.string.home_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
        )

        Spacer(modifier = Modifier.height(20.dp))

        ModesCards(
            gameModes = gameModes,
            selectedMode = selectedMode,
            onModeSelected = { mode ->
                selectedMode = mode
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        PlayerInputs(
            players = players
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                homeViewModel.startGame(
                    selectedMode = selectedMode,
                    playerNames = players,
                    onSuccess = { gameId ->
                        navController.navigate("${AppScreens.GAME.name}/$gameId")
                    },
                    onError = { message ->
                        errorMessage = message
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Text(" ${stringResource(R.string.start_game)}")
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
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        gameModes.chunked(2).forEach { rowItems ->
            if (rowItems.size == 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowItems.forEach { (icon, title, subtitle) ->
                        GameModeCard(
                            icon = icon,
                            title = title,
                            subtitle = subtitle,
                            isSelected = selectedMode == title,
                            modifier = Modifier.weight(1f),
                            onClick = { onModeSelected(title) }
                        )
                    }
                }
            } else {
                // Ungerade Anzahl: letztes Element zentriert
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    val (icon, title, subtitle) = rowItems[0]
                    GameModeCard(
                        icon = icon,
                        title = title,
                        subtitle = subtitle,
                        isSelected = selectedMode == title,
                        modifier = Modifier.widthIn(max = 220.dp),
                        onClick = { onModeSelected(title) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerInputs(
    players: MutableList<String>,
) {
    Text(
        text = stringResource(R.string.section_players),
        style = MaterialTheme.typography.titleMedium,
    )

    Spacer(Modifier.height(10.dp))

    players.forEachIndexed { index, name ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerInputField(
                value = name,
                placeholder = stringResource(R.string.player_placeholder, index + 1),
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
                        contentDescription = stringResource(R.string.remove_player),
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
        Text(" ${stringResource(R.string.add_player)}", color = MaterialTheme.colorScheme.onSurface)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DartScoreTheme {
        HomeScreen(navController = NavHostController(LocalContext.current))
    }
}
