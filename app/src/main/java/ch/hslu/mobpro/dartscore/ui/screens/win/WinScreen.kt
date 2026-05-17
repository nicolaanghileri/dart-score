package ch.hslu.mobpro.dartscore.ui.screens.win

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ch.hslu.mobpro.dartscore.ui.navigation.AppScreens
import ch.hslu.mobpro.dartscore.ui.theme.DartScoreTheme
import ch.hslu.mobpro.dartscore.R
@Composable
fun WinScreen(
    winnerName: String,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.trophy),
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.game_finished),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.winner_wins, winnerName),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.congratulations_checkout),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navController.navigate(AppScreens.HOME.name) {
                    popUpTo(AppScreens.HOME.name) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Home, contentDescription = null)
            Text(" ${stringResource(R.string.back_to_home)}")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                navController.navigate(AppScreens.STATS.name)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Icon(
                Icons.Outlined.BarChart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.view_stats),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WinScreenPreview() {
    DartScoreTheme {
        WinScreen(
            winnerName = "Thomas",
            navController = NavHostController(LocalContext.current)
        )
    }
}