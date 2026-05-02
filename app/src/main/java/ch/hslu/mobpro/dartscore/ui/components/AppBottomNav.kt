package ch.hslu.mobpro.dartscore.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.dartscore.ui.theme.BottomNavNormal
import ch.hslu.mobpro.dartscore.ui.theme.BottomNavNormalContent
import ch.hslu.mobpro.dartscore.ui.theme.BottomNavSelected
import ch.hslu.mobpro.dartscore.ui.theme.BottomNavSelectedContent
import ch.hslu.mobpro.dartscore.ui.theme.DartScoreTheme

@Preview(showBackground = true)
@Composable
fun AppBottomNavPreview() {
    DartScoreTheme {
        AppBottomNav(
            selectedIndex = 0,
            onItemClick = {},
        )
    }
}


data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)


@Composable
fun AppBottomNav(
    selectedIndex: Int,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Outlined.Home
        ),
        BottomNavItem(
            label = "Stats",
            icon = Icons.Outlined.QueryStats
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .padding(horizontal = 24.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                AppBottomNavItem(
                    item = item,
                    selected = selectedIndex == index,
                    onClick = { onItemClick(index) }
                )
            }
        }
    }
}

@Composable
private fun AppBottomNavItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor =
        if (selected) BottomNavSelected else BottomNavNormal

    val contentColor =
        if (selected) BottomNavSelectedContent else BottomNavNormalContent

    Column(
        modifier = Modifier
            .width(90.dp)
            .height(64.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = contentColor,
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.label,
            color = contentColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}