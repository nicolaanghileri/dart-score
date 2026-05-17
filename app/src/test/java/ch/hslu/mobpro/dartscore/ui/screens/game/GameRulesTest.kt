package ch.hslu.mobpro.dartscore.ui.screens.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GameRulesTest {

    @Test
    fun testInitialScore301() {
        assertEquals(301, GameRules.initialScore("301"))
    }

    @Test
    fun testInitialScore501() {
        assertEquals(501, GameRules.initialScore("501"))
    }

    @Test
    fun testInitialScore701() {
        assertEquals(701, GameRules.initialScore("701"))
    }

    @Test
    fun testInitialScoreDefault() {
        assertEquals(501, GameRules.initialScore("abc"))
    }

    @Test
    fun testIsBustLowerAs0() {
        val result = GameRules.isBust(
            scoreAfterDart = -1,
            dart = ScoredDart(score = 20, isDouble = true)
        )

        assertTrue(result)
    }

    @Test
    fun testIsBust1() {
        val result = GameRules.isBust(
            scoreAfterDart = 1,
            dart = ScoredDart(score = 20, isDouble = true)
        )

        assertTrue(result)
    }

    @Test
    fun testIsBust0NotDouble() {
        val result = GameRules.isBust(
            scoreAfterDart = 0,
            dart = ScoredDart(score = 20, isDouble = false)
        )

        assertTrue(result)
    }

    @Test
    fun testIsBustOk() {
        val result = GameRules.isBust(
            scoreAfterDart = 0,
            dart = ScoredDart(score = 20, isDouble = true)
        )

        assertFalse(result)
    }

    @Test
    fun testPadWithZeroes() {
        val result = with(GameRules) {
            listOf(20, 5).padWithZeroes()
        }

        assertEquals(listOf(20, 5, 0), result)
    }
}