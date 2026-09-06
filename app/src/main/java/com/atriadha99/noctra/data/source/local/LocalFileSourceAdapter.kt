package com.atriadha99.noctra.data.source.local

import android.content.Context
import android.provider.MediaStore
import com.atriadha99.noctra.domain.model.Playlist
import com.atriadha99.noctra.domain.model.Track
import com.atriadha99.noctra.domain.source.MusicSource
import com.atriadha99.noctra.domain.source.SourceCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalFileSourceAdapter @Inject constructor(
    @ApplicationContext private val context: Context
) : MusicSource {
    override val sourceId: String = "local_files"
    override val capabilities: SourceCapabilities = SourceCapabilities(
        canStreamDirectly = true,
        supportsCustomDsp = true,
        requiresHostAppRunning = false
    )

    override suspend fun search(query: String): List<Track> {
        return getAllLocalTracks().filter {
            it.title.contains(query, ignoreCase = true) || 
            it.artist.contains(query, ignoreCase = true)
        }
    }

    override suspend fun getPlaylist(id: String): Playlist {
        val tracks = getAllLocalTracks()
        return Playlist(
            id = id,
            sourceId = sourceId,
            title = "Local Music",
            description = "Local device music",
            coverUrl = null,
            tracks = tracks
        )
    }
    
    suspend fun getAllLocalTracks(): List<Track> = withContext(Dispatchers.IO) {
        val tracks = mutableListOf<Track>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )
        
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        
        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val data = cursor.getString(dataColumn)
                val duration = cursor.getLong(durationColumn)
                val albumId = cursor.getLong(albumIdColumn)
                
                // Art URI placeholder for local media store
                val artUri = "content://media/external/audio/albumart/$albumId"
                
                tracks.add(
                    Track(
                        id = id.toString(),
                        sourceId = sourceId,
                        title = title,
                        artist = artist,
                        durationMs = duration,
                        streamUrl = data, // File path
                        coverUrl = artUri
                    )
                )
            }
        }
        tracks
    }

    override fun play(track: Track) {
        // Will be handled by PlaybackManager directly
    }

    override fun pause() {
        // Will be handled by PlaybackManager directly
    }

    override fun seekTo(positionMs: Long) {
        // Will be handled by PlaybackManager directly
    }
}
