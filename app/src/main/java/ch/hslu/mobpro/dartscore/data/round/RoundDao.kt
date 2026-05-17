package ch.hslu.mobpro.dartscore.data.round
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoundDao {

    @Insert
    suspend fun insertRound(round: RoundEntity)

    @Query("SELECT * FROM round WHERE game_id = :gameId")
    suspend fun getRoundsByGameId(gameId: Int): List<RoundEntity>

    @Query("SELECT * FROM round WHERE player_id = :playerId AND game_id = :gameId")
    suspend fun getRoundsByPlayerAndGameId(playerId: Int, gameId: Int): List<RoundEntity>


}

