package ch.hslu.mobpro.dartscore.data.player

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerDao {

    @Insert
    suspend fun insertPlayer(player: PlayerEntity)
    @Insert
    suspend fun insertPlayers(players: List<PlayerEntity>)

    @Query("UPDATE player SET score = :score WHERE id = :playerId")
    suspend fun updatePlayerScore(playerId: Int, score: Int)

    @Query("SELECT * FROM player WHERE game_id = :gameId")
    suspend fun getPlayersByGameId(gameId: Int): List<PlayerEntity>

}