package ch.hslu.mobpro.dartscore.data.repository

import ch.hslu.mobpro.dartscore.data.player.PlayerDao
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity

class PlayerRepository(
    private val playerDao: PlayerDao
) {

    suspend fun insertPlayer(player: PlayerEntity) {
        playerDao.insertPlayer(player)
    }
    suspend fun insertPlayers(players: List<PlayerEntity>) {
        playerDao.insertPlayers(players)
    }

}