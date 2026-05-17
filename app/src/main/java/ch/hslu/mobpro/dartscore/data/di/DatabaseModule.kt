package ch.hslu.mobpro.dartscore.data.di

import android.content.Context
import ch.hslu.mobpro.dartscore.data.DartScoreDatabase
import ch.hslu.mobpro.dartscore.data.game.GameDao
import ch.hslu.mobpro.dartscore.data.player.PlayerDao
import ch.hslu.mobpro.dartscore.data.round.RoundDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DartScoreDatabase {
        return DartScoreDatabase.getDatabase(context)
    }

    @Provides
    fun provideGameDao(database: DartScoreDatabase): GameDao {
        return database.gameDao()
    }

    @Provides
    fun providePlayerDao(database: DartScoreDatabase): PlayerDao {
        return database.playerDao()
    }

    @Provides
    fun provideRoundDao(database: DartScoreDatabase): RoundDao {
        return database.roundDao()
    }
}
