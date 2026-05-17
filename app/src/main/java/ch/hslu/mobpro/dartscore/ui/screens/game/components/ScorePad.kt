package ch.hslu.mobpro.dartscore.ui.screens.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.hslu.mobpro.dartscore.ui.theme.DartScoreTheme

@Preview(showBackground = true)
@Composable
fun ScorePadPreview() {
    DartScoreTheme {
        ScorePad(
            modifier = Modifier.padding(16.dp),
            onScoreClick = { _, _ -> },
            onDeleteClick = {}
        )
    }
}

enum class ScoreMultiplier(
    val label: String,
    val value: Int
) {
    SINGLE("Single", 1),
    DOUBLE("Double", 2),
    TRIPLE("Triple", 3)
}

@Composable
fun ScorePad(
    onScoreClick: (score: Int, isDouble: Boolean) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMultiplier by rememberSaveable {
        mutableStateOf(ScoreMultiplier.SINGLE)
    }

    val rows = listOf(
        listOf(1, 2, 3, 4, 5),
        listOf(6, 7, 8, 9, 10),
        listOf(11, 12, 13, 14, 15),
        listOf(16, 17, 18, 19, 20)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ScoreMultiplier.entries.forEach { multiplier ->
                ScorePadTile(
                    text = multiplier.label,
                    selected = selectedMultiplier == multiplier,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    onClick = {
                        selectedMultiplier = multiplier
                    }
                )
            }
        }

        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { target ->
                    ScorePadTile(
                        text = target.toString(),
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        onClick = {
                            val score = target * selectedMultiplier.value
                            onScoreClick(score, selectedMultiplier == ScoreMultiplier.DOUBLE)
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ScorePadTile(
                text = "25",
                enabled = selectedMultiplier != ScoreMultiplier.TRIPLE,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = {
                    onScoreClick(
                        25 * selectedMultiplier.value,
                        selectedMultiplier == ScoreMultiplier.DOUBLE
                    )
                }
            )

            ScorePadTile(
                text = "0",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = {
                    onScoreClick(0, false)
                }
            )

            ScorePadTile(
                icon = Icons.AutoMirrored.Outlined.Backspace,
                text = "Delete",
                danger = true,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = onDeleteClick
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }
    }
}
