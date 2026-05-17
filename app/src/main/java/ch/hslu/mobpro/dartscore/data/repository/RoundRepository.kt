package ch.hslu.mobpro.dartscore.data.repository

import ch.hslu.mobpro.dartscore.data.round.RoundDao
import ch.hslu.mobpro.dartscore.data.round.RoundEntity
import javax.inject.Inject

class RoundRepository @Inject constructor(
    private val roundDao: RoundDao
) {

    suspend fun insertRound(round: RoundEntity) {
        roundDao.insertRound(round)
    }

    suspend fun getRoundsByGameId(gameId: Int): List<RoundEntity> {
        return roundDao.getRoundsByGameId(gameId)
    }

    suspend fun getRoundsByPlayerAndGameId(playerId: Int, gameId: Int): List<RoundEntity> {
        return roundDao.getRoundsByPlayerAndGameId(playerId, gameId)
    }
}
