package ch.hslu.mobpro.dartscore.ui.screens.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import ch.hslu.mobpro.dartscore.ui.theme.DangerRed
import ch.hslu.mobpro.dartscore.ui.theme.TileBorder
import ch.hslu.mobpro.dartscore.ui.theme.Transparent
import ch.hslu.mobpro.dartscore.ui.theme.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.dartscore.ui.theme.DartScoreTheme


@Preview(showBackground = true)
@Composable
fun ScorePadTilePreview() {
    DartScoreTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ScorePadTile(
                text = "20",
                onClick = {}
            )

            ScorePadTile(
                text = "Single",
                selected = true,
                modifier = Modifier.width(128.dp),
                onClick = {}
            )

            ScorePadTile(
                icon = Icons.AutoMirrored.Outlined.Backspace,
                text = "Delete",
                danger = true,
                onClick = {}
            )
        }
    }
}
@Composable
fun ScorePadTile(
    text: String? = null,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    danger: Boolean = false,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(16.dp)

    val backgroundColor = when {
        danger -> DangerRed
        selected -> MaterialTheme.colorScheme.primary
        else -> White
    }

    val contentColor = when {
        danger -> White
        selected -> MaterialTheme.colorScheme.onPrimary
        enabled -> MaterialTheme.colorScheme.onBackground
        else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.35f)
    }

    val borderColor =
        if (selected || danger) Transparent
        else TileBorder

    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(enabled = enabled) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
        } else if (text != null) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                color = contentColor
            )
        }
    }
}
