package ch.hslu.mobpro.dartscore.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ch.hslu.mobpro.dartscore.data.game.GameDao
import ch.hslu.mobpro.dartscore.data.game.GameEntity
import ch.hslu.mobpro.dartscore.data.player.PlayerDao
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity
import ch.hslu.mobpro.dartscore.data.round.RoundDao
import ch.hslu.mobpro.dartscore.data.round.RoundEntity


@Database(
    entities = [
        GameEntity::class,
        PlayerEntity::class,
        RoundEntity::class
    ],
    version = 2
)
abstract class DartScoreDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao
    abstract fun playerDao(): PlayerDao
    abstract fun roundDao(): RoundDao
}