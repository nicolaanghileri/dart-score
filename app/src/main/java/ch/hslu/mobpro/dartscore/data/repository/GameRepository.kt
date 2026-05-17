package ch.hslu.mobpro.dartscore.data.repository

import ch.hslu.mobpro.dartscore.data.game.GameDao
import ch.hslu.mobpro.dartscore.data.game.GameEntity
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val gameDao: GameDao
) {

    suspend fun insertGame(game: GameEntity) : Long {
        return gameDao.insertGame(game)
    }

    suspend fun getAllGames(): List<GameEntity> {
        return gameDao.getAllGames()
    }

    suspend fun getGameById(gameId: Int): GameEntity? {
        return gameDao.getGameById(gameId)
    }

    suspend fun finishGame(gameId: Int, winnerPlayerId: Int) {
        gameDao.finishGame(
            gameId = gameId,
            winnerPlayerId = winnerPlayerId,
            status = "finished"
        )
    }
}
