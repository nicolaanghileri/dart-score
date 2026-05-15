package ch.hslu.mobpro.dartscore.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ch.hslu.mobpro.dartscore.ui.screens.game.GameScreen
import ch.hslu.mobpro.dartscore.ui.screens.home.HomeScreen
import ch.hslu.mobpro.dartscore.ui.screens.stats.StatsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppScreens.HOME.name
    ) {
        composable(route = AppScreens.HOME.name) {
            HomeScreen(modifier, navController)
        }

        composable(
            route = "${AppScreens.GAME.name}/{gameId}",
            arguments = listOf(
                navArgument("gameId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")
            GameScreen(
                modifier,
                navController,
                gameId = gameId?.toInt() ?: 0
            )
        }

        composable(route = AppScreens.STATS.name) {
            StatsScreen()
        }
    }
}

enum class AppScreens {
    HOME,
    GAME,
    STATS
}
