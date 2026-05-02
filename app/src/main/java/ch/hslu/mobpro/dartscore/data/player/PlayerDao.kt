package ch.hslu.mobpro.dartscore.data.player

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerDao {

    @Insert
    suspend fun insertPlayer(player: PlayerEntity)

    @Delete
    suspend fun deletePlayer(player: PlayerEntity)

    @Query("UPDATE player SET score = :score WHERE id = :playerId")
    suspend fun updatePlayerScore(playerId: Int, score: Int)

    @Query("UPDATE player SET is_winner = :isWinner WHERE id = :playerId")
    suspend fun updatePlayerWinner(playerId: Int, isWinner: Boolean)

    @Query("SELECT * FROM player WHERE game_id = :gameId")
    suspend fun getPlayersByGameId(gameId: Int): List<PlayerEntity>

    @Query("SELECT * FROM player WHERE id = :playerId")
    suspend fun getPlayerById(playerId: Int): PlayerEntity?

}