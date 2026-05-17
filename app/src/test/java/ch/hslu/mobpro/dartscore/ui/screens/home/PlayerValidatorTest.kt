package ch.hslu.mobpro.dartscore.ui.screens.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PlayerValidatorTest {
    @Test
    fun testValidatePlayerNamesEmpty() {
        val result = PlayerValidator.validatePlayerNames(listOf("", "Thomas"))

        assertEquals("Player name cannot be empty", result)
    }

    @Test
    fun testValidatePlayerNamesNotEnoughCharacters() {
        val result = PlayerValidator.validatePlayerNames(listOf("To", "Nicola"))

        assertEquals("Player names must have at least 3 characters", result)
    }

    @Test
    fun testValidatePlayerLessThan2Players() {
        val result = PlayerValidator.validatePlayerNames(listOf("Thomas"))

        assertEquals("At least 2 players are required", result)
    }

    @Test
    fun testValidatePlayerNotUnique() {
        val result = PlayerValidator.validatePlayerNames(listOf("Thomas", "thomas"))

        assertEquals("Player names must be unique", result)
    }

    @Test
    fun testValidatePlayerOk() {
        val result = PlayerValidator.validatePlayerNames(listOf("Thomas", "Nicola"))

        assertNull(result)
    }
}