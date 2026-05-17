package ch.hslu.mobpro.dartscore.data.game

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(game: GameEntity): Long

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Query("UPDATE game SET status = :status WHERE id = :gameId")
    suspend fun updateGameStatus(gameId: Int, status: String)

    @Query("UPDATE game SET status = :status, winner_player_id = :winnerPlayerId WHERE id = :gameId")
    suspend fun finishGame(gameId: Int, winnerPlayerId: Int, status: String)

    @Query("SELECT * FROM game")
    suspend fun getAllGames(): List<GameEntity>

    @Query("SELECT * FROM game WHERE id = :gameId")
    suspend fun getGameById(gameId: Int): GameEntity?

    @Query("DELETE FROM game WHERE status = 'started'")
    suspend fun deleteGamesInProgress()
}
