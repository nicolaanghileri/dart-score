package ch.hslu.mobpro.dartscore.data.round
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoundDao {

    @Insert
    suspend fun insertRound(round: RoundEntity)

    @Delete
    suspend fun deleteRound(round: RoundEntity)

    @Query("SELECT * FROM round WHERE game_id = :gameId")
    suspend fun getRoundsByGameId(gameId: Int): List<RoundEntity>

    @Query("SELECT * FROM round WHERE id = :roundId")
    suspend fun getRoundById(roundId: Int): RoundEntity?

    @Query("SELECT * FROM round WHERE player_id = :playerId AND game_id = :gameId")
    suspend fun getRoundsByPlayerAndGameId(playerId: Int, gameId: Int): List<RoundEntity>

    @Query("UPDATE round SET dart1 = :dart1, dart2 = :dart2, dart3 = :dart3 WHERE id = :roundId")
    suspend fun updateRoundDarts(roundId: Int, dart1: Int, dart2: Int, dart3: Int)

}

