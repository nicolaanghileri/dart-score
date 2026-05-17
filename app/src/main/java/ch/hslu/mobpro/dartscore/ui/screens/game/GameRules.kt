package ch.hslu.mobpro.dartscore.ui.screens.game


object GameRules {
    const val DARTS_PER_ROUND = 3

    fun initialScore(gameType: String): Int = when (gameType) {
        "301" -> 301
        "701" -> 701
        else -> 501
    }

    fun isBust(scoreAfterDart: Int, dart: ScoredDart): Boolean {
        return scoreAfterDart < 0 ||
                scoreAfterDart == 1 ||
                (scoreAfterDart == 0 && !dart.isDouble)
    }

    fun List<Int>.padWithZeroes(): List<Int> {
        return this + List(DARTS_PER_ROUND - size) { 0 }
    }
}