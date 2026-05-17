package ch.hslu.mobpro.dartscore.ui.screens.stats

data class GameSummaryUiModel(
    val id: Int,
    val date: String,
    val playersText: String,
    val winnerText: String,
    val throws: Int,
    val avgScore: Int,
    val highest: Int
)