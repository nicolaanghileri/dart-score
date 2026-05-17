package ch.hslu.mobpro.dartscore.data.repository

import ch.hslu.mobpro.dartscore.data.player.PlayerDao
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity
import javax.inject.Inject

class PlayerRepository @Inject constructor(
    private val playerDao: PlayerDao
) {

    suspend fun insertPlayer(player: PlayerEntity) {
        playerDao.insertPlayer(player)
    }
    suspend fun insertPlayers(players: List<PlayerEntity>) {
        playerDao.insertPlayers(players)
    }

    suspend fun getPlayersByGameId(gameId: Int): List<PlayerEntity> {
        return playerDao.getPlayersByGameId(gameId)
    }

    suspend fun updatePlayerScore(playerId: Int, score: Int) {
        playerDao.updatePlayerScore(playerId, score)
    }

}
