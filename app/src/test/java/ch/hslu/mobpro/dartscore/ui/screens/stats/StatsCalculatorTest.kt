package ch.hslu.mobpro.dartscore.ui.screens.stats


import org.junit.Assert.assertEquals
import org.junit.Test

class StatsCalculatorTest {

    @Test
    fun testAverageScoreEmptyList() {
        val result = StatsCalculator.averageScore(emptyList())

        assertEquals(0, result)
    }

    @Test
    fun testAverageScoreWithValues() {
        val result = StatsCalculator.averageScore(listOf(60, 45, 100))

        assertEquals(68, result)
    }

    @Test
    fun testHighestScoreEmptyList() {
        val result = StatsCalculator.highestScore(emptyList())

        assertEquals(0, result)
    }

    @Test
    fun testHighestScoreWithValues() {
        val result = StatsCalculator.highestScore(listOf(60, 45, 100))

        assertEquals(100, result)
    }

    @Test
    fun testThrowCountZeroRounds() {
        val result = StatsCalculator.throwCount(0)

        assertEquals(0, result)
    }

    @Test
    fun testThrowCountWithRounds() {
        val result = StatsCalculator.throwCount(4)

        assertEquals(12, result)
    }
}