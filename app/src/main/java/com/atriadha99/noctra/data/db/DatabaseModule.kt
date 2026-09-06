package com.atriadha99.noctra.data.db

import android.content.Context
import androidx.room.Room
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
    fun provideNoctraDatabase(
        @ApplicationContext context: Context
    ): NoctraDatabase {
        return Room.databaseBuilder(
            context,
            NoctraDatabase::class.java,
            "noctra_music.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTrackDao(database: NoctraDatabase): TrackDao {
        return database.trackDao()
    }

    @Provides
    fun providePlaylistDao(database: NoctraDatabase): PlaylistDao {
        return database.playlistDao()
    }
}
