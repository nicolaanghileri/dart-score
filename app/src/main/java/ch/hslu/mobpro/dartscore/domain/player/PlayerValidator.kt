package ch.hslu.mobpro.dartscore.domain.player

object PlayerValidator {
    fun validatePlayerNames(names: List<String>): String? {
        val cleanedNames = names.map { it.trim() }

        return when {
            cleanedNames.any { it.isBlank() } ->
                "Player name cannot be empty"

            cleanedNames.any { it.length < 3 } ->
                "Player names must have at least 3 characters"

            cleanedNames.size < 2 ->
                "At least 2 players are required"

            cleanedNames.map { it.lowercase() }.distinct().size != cleanedNames.size ->
                "Player names must be unique"

            else -> null
        }
    }
}