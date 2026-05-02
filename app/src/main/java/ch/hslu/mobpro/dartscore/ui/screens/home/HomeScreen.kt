package ch.hslu.mobpro.dartscore.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ch.hslu.mobpro.dartscore.ui.screens.home.components.GameModeCard
import ch.hslu.mobpro.dartscore.ui.screens.home.components.PlayerInputField
import ch.hslu.mobpro.dartscore.ui.theme.DartScoreTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
){
    var selectedMode by remember { mutableStateOf("501") }
    val players = remember { mutableStateListOf("", "") }

    val gameModes = listOf(
        Triple(Icons.Outlined.GpsFixed, "501", "Classic 501 game"),
        Triple(Icons.Outlined.Bolt, "301", "Fast 301 game"),
        Triple(Icons.Outlined.EmojiEvents, "701", "Extended 701 game"),
        Triple(Icons.Outlined.GpsFixed, "Cricket", "Cricket scoring"),
    )

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ){
        Text(
            text = "New Darts Game",
            style = MaterialTheme.typography.titleLarge,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            items(gameModes) { (icon, title, subtitle) ->
                GameModeCard(
                    icon = icon,
                    title = title,
                    subtitle = subtitle,
                    isSelected = selectedMode == title,
                    onClick = { selectedMode = title }
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text("Players")

        Spacer(Modifier.height(8.dp))

        players.forEachIndexed { index, name ->
            PlayerInputField(
                value = name,
                onValueChange = { players[index] = it }
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = { players.add("") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
            Text(" Add Player", color = MaterialTheme.colorScheme.onSurface)
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { /* TNavigation zum GameScreen */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
            Text(" Start Game", color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    DartScoreTheme {
        HomeScreen()
    }
}