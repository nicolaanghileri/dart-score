package ch.hslu.mobpro.dartscore.data.repository

import ch.hslu.mobpro.dartscore.data.game.GameDao
import ch.hslu.mobpro.dartscore.data.game.GameEntity

class GameRepository(
    private val gameDao: GameDao
) {

    suspend fun insertGame(game: GameEntity) : Long {
        return gameDao.insertGame(game)
    }
}