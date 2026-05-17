package ch.hslu.mobpro.dartscore.domain.stats

data class StatsUiState(
    val summaries: List<GameSummaryUiModel> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)