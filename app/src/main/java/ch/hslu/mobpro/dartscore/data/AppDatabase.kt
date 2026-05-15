package ch.hslu.mobpro.dartscore.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ch.hslu.mobpro.dartscore.data.game.GameDao
import ch.hslu.mobpro.dartscore.data.game.GameEntity
import ch.hslu.mobpro.dartscore.data.player.PlayerDao
import ch.hslu.mobpro.dartscore.data.player.PlayerEntity
import ch.hslu.mobpro.dartscore.data.round.RoundDao
import ch.hslu.mobpro.dartscore.data.round.RoundEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor


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
    companion object {
        private const val DB_NAME = "darts-score"
        private var INSTANCE: DartScoreDatabase? = null
        fun getDatabase(context: Context): DartScoreDatabase {
            return INSTANCE ?: buildDatabase(context).also { INSTANCE = it
            }
        }
        private fun buildDatabase(context: Context): DartScoreDatabase {
            val ioDispatcherExecutor = Dispatchers.IO.asExecutor()
            return Room
                .databaseBuilder(
                    context,
                    DartScoreDatabase::class.java,
                    DB_NAME
                )
                .setQueryExecutor(ioDispatcherExecutor)
                .setTransactionExecutor(ioDispatcherExecutor)
                .fallbackToDestructiveMigration(true)  // Remove this in production
                .build()
        }
    }
}