package ch.hslu.mobpro.dartscore.domain.stats

data class GameSummaryUiModel(
    val id: Int,
    val date: String,
    val playersText: String,
    val winnerText: String,
    val throws: Int,
    val avgScore: Int,
    val highest: Int
)