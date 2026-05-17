package ch.hslu.mobpro.dartscore.ui.screens.stats

data class StatsUiState(
    val summaries: List<GameSummaryUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)