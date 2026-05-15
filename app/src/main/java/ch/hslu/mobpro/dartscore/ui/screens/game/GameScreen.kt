package ch.hslu.mobpro.dartscore.ui.screens.game

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    gameId: Int
){
    Text("Game")
}