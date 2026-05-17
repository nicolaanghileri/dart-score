package ch.hslu.mobpro.dartscore.ui.screens.game

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import ch.hslu.mobpro.dartscore.ui.theme.InputDisplayBackground
import ch.hslu.mobpro.dartscore.ui.theme.InputDisplayText
import ch.hslu.mobpro.dartscore.ui.theme.Navy
import ch.hslu.mobpro.dartscore.ui.theme.ShadowOverlay
import ch.hslu.mobpro.dartscore.ui.theme.White
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.res.stringResource
import ch.hslu.mobpro.dartscore.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import ch.hslu.mobpro.dartscore.ui.components.AppErrorDialog
import ch.hslu.mobpro.dartscore.ui.navigation.AppScreens
import ch.hslu.mobpro.dartscore.ui.screens.game.components.ScorePad
import ch.hslu.mobpro.dartscore.ui.theme.DartScoreTheme

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    gameId: Int
){
    val gameViewModel: GameViewModel = hiltViewModel<GameViewModel>()
    val uiState by gameViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isGameFinished, uiState.winnerName) {
        if (uiState.isGameFinished && uiState.winnerName != null) {
            val encodedWinnerName = Uri.encode(uiState.winnerName)

            navController.navigate("${AppScreens.WIN.name}/$encodedWinnerName") {
                popUpTo("${AppScreens.GAME.name}/$gameId") {
                    inclusive = true
                }
            }
        }
    }

    AppErrorDialog(
        message = uiState.errorMessage,
        onDismiss = gameViewModel::clearError
    )

    if (uiState.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    GameScreenContent(
        modifier = modifier,
        gameType = uiState.gameType,
        pointsRemaining = uiState.pointsRemaining.coerceAtLeast(0),
        currentPlayerName = uiState.currentPlayer?.name ?: stringResource(R.string.player_fallback),
        winnerName = uiState.winnerName,
        isGameFinished = uiState.isGameFinished,
        currentInput = uiState.currentDarts.joinToString(" + ") { it.score.toString() },
        dartCount = uiState.currentDarts.size,
        onScoreClick = gameViewModel::addScore,
        onDeleteClick = gameViewModel::deleteLastDart,
        onResetClick = gameViewModel::clearCurrentDarts
    )
}

@Composable
private fun GameScreenContent(
    gameType: String,
    pointsRemaining: Int,
    currentPlayerName: String,
    winnerName: String?,
    isGameFinished: Boolean,
    currentInput: String,
    dartCount: Int,
    onScoreClick: (score: Int, isDouble: Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
    ) {
        GameHeader(
            gameType = gameType,
            pointsRemaining = pointsRemaining,
            currentPlayerName = currentPlayerName,
            winnerName = winnerName,
            isGameFinished = isGameFinished,
            onResetClick = onResetClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        ScoreInputDisplay(
            text = if (isGameFinished) stringResource(R.string.game_finished) else currentInput.ifBlank { stringResource(R.string.enter_score) },
            dartCount = dartCount,
            isGameFinished = isGameFinished
        )

        Spacer(modifier = Modifier.height(12.dp))

        ScorePad(
            modifier = Modifier.padding(horizontal = 16.dp),
            onScoreClick = { score, isDouble ->
                if (!isGameFinished) {
                    onScoreClick(score, isDouble)
                }
            },
            onDeleteClick = onDeleteClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun GameHeader(
    gameType: String,
    pointsRemaining: Int,
    currentPlayerName: String,
    winnerName: String?,
    isGameFinished: Boolean,
    onResetClick: () -> Unit
) {
    val shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .shadow(
                elevation = 16.dp,
                shape = shape,
                spotColor = ShadowOverlay
            ),
        color = Navy,
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 26.dp, top = 28.dp, end = 24.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.GpsFixed,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.height(30.dp)
                )

                Text(
                    text = stringResource(R.string.game_type_header, gameType),
                    color = White,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .weight(1f)
                )

                IconButton(onClick = onResetClick) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = stringResource(R.string.clear_current_darts),
                        tint = White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isGameFinished) {
                    winnerName ?: currentPlayerName
                } else {
                    currentPlayerName
                },
                color = White,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = pointsRemaining.toString(),
                color = White,
                fontSize = 60.sp,
                lineHeight = 60.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.points_remaining),
                color = White.copy(alpha = 0.66f),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun ScoreInputDisplay(
    text: String,
    dartCount: Int,
    isGameFinished: Boolean
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 22.dp)
            .fillMaxWidth()
            .height(82.dp)
            .background(
                color = InputDisplayBackground,
                shape = RoundedCornerShape(28.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            color = InputDisplayText,
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = if (isGameFinished) stringResource(R.string.winner_confirmed)
                   else stringResource(R.string.dart_x_of_3, (dartCount + 1).coerceAtMost(3)),
            color = InputDisplayText.copy(alpha = 0.78f),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenContentPreview() {
    DartScoreTheme {
        GameScreenContent(
            gameType = "501",
            pointsRemaining = 501,
            currentPlayerName = "Nico",
            winnerName = null,
            isGameFinished = false,
            currentInput = "",
            dartCount = 0,
            onScoreClick = { _, _ -> },
            onDeleteClick = {},
            onResetClick = {}
        )
    }
}
