package ch.hslu.mobpro.dartscore.domain.stats

import ch.hslu.mobpro.dartscore.domain.game.GameRules

object StatsCalculator {
    fun averageScore(roundScores: List<Int>): Int {
        return if (roundScores.isEmpty()) 0 else roundScores.average().toInt()
    }

    fun highestScore(roundScores: List<Int>): Int {
        return roundScores.maxOrNull() ?: 0
    }

    fun throwCount(roundCount: Int): Int {
        return roundCount * GameRules.DARTS_PER_ROUND
    }
}