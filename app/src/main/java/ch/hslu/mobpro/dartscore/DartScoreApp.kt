package ch.hslu.mobpro.dartscore

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ch.hslu.mobpro.dartscore.ui.components.AppBottomNav
import ch.hslu.mobpro.dartscore.ui.navigation.AppNavHost
import ch.hslu.mobpro.dartscore.ui.navigation.AppScreens

@Composable
fun DartScoreApp() {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val selectedIndex = when (currentRoute) {
        AppScreens.HOME.name,
        "${AppScreens.GAME.name}/{gameId}" -> 0
        AppScreens.STATS.name -> 1
        else -> 0
    }

    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        bottomBar = {
            AppBottomNav(
                selectedIndex = selectedIndex,
                onItemClick = { index ->
                    when (index) {
                        0 -> navController.navigate(AppScreens.HOME.name)
                        1 -> navController.navigate(AppScreens.STATS.name)
                    }
                }
            )
        }
    ) { padding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.
            fillMaxSize()
                .padding(
                    start = padding.calculateStartPadding(layoutDirection),
                    end = padding.calculateEndPadding(layoutDirection),
                    bottom = 0.dp
                )
        )
    }
}
