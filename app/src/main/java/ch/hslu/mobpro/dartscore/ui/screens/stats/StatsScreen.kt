package ch.hslu.mobpro.dartscore.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ch.hslu.mobpro.dartscore.ui.screens.stats.components.GameSummaryCard
import ch.hslu.mobpro.dartscore.ui.theme.DartScoreTheme

@Composable
fun StatsScreen(
    modifier: Modifier = Modifier
) {
    val statsViewModel: StatsViewModel = hiltViewModel()
    val uiState by statsViewModel.uiState.collectAsState()

    StatsScreenContent(
        modifier = modifier,
        uiState = uiState
    )
}

@Composable
private fun StatsScreenContent(
    uiState: StatsUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.errorMessage != null -> {
                EmptyStateText(
                    title = "Could not load stats",
                    body = uiState.errorMessage,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.summaries.isEmpty() -> {
                EmptyStateText(
                    title = "No games yet",
                    body = "Finished games will show up here.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        start = 16.dp,
                        top = 24.dp,
                        end = 16.dp,
                        bottom = 112.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column {
                            Text(
                                text = "Game Stats",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${uiState.summaries.size} games tracked",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f)
                            )
                        }
                    }

                    items(
                        items = uiState.summaries,
                        key = { it.id }
                    ) { summary ->
                        GameSummaryCard(
                            date = summary.date,
                            playersText = summary.playersText,
                            winnerText = summary.winnerText,
                            throws = summary.throws,
                            avgScore = summary.avgScore,
                            highest = summary.highest
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateText(
    title: String,
    body: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.58f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsScreenContentPreview() {
    DartScoreTheme {
        StatsScreenContent(
            uiState = StatsUiState(
                summaries = listOf(
                    GameSummaryUiModel(
                        id = 1,
                        date = "May 16, 2026",
                        playersText = "Nico vs Alex",
                        winnerText = "Nico",
                        throws = 24,
                        avgScore = 63,
                        highest = 140
                    )
                ),
                isLoading = false
            )
        )
    }
}
