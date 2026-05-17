package ch.hslu.mobpro.dartscore.data.di

import android.content.Context
import androidx.room.Room
import ch.hslu.mobpro.dartscore.data.DartScoreDatabase
import ch.hslu.mobpro.dartscore.data.game.GameDao
import ch.hslu.mobpro.dartscore.data.player.PlayerDao
import ch.hslu.mobpro.dartscore.data.round.RoundDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "darts-score"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DartScoreDatabase {
        val ioDispatcherExecutor = Dispatchers.IO.asExecutor()

        return Room.databaseBuilder(
            context,
            DartScoreDatabase::class.java,
            DB_NAME
        )
            .setQueryExecutor(ioDispatcherExecutor)
            .setTransactionExecutor(ioDispatcherExecutor)
            //.fallbackToDestructiveMigration(true) // just for testing
            .build()
    }

    @Provides
    fun provideGameDao(database: DartScoreDatabase): GameDao = database.gameDao()

    @Provides
    fun providePlayerDao(database: DartScoreDatabase): PlayerDao = database.playerDao()

    @Provides
    fun provideRoundDao(database: DartScoreDatabase): RoundDao = database.roundDao()
}
