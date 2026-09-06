package com.atriadha99.noctra.data.source.local

import com.atriadha99.noctra.domain.model.Playlist
import com.atriadha99.noctra.domain.model.Track
import com.atriadha99.noctra.domain.source.MusicSource
import com.atriadha99.noctra.domain.source.SourceCapabilities
import javax.inject.Inject

class LocalFileSourceAdapter @Inject constructor() : MusicSource {
    override val sourceId: String = "local_files"
    override val capabilities: SourceCapabilities = SourceCapabilities(
        canStreamDirectly = true,
        supportsCustomDsp = true,
        requiresHostAppRunning = false
    )

    override suspend fun search(query: String): List<Track> {
        // TODO: Implement actual media store query
        return emptyList()
    }

    override suspend fun getPlaylist(id: String): Playlist {
        // TODO: Implement actual media store query
        return Playlist(
            id = id,
            sourceId = sourceId,
            title = "Local Music",
            description = "Local device music",
            coverUrl = null,
            tracks = emptyList()
        )
    }

    override fun play(track: Track) {
        // TODO: Send command to MediaSessionService
    }

    override fun pause() {
        // TODO: Send command to MediaSessionService
    }

    override fun seekTo(positionMs: Long) {
        // TODO: Send command to MediaSessionService
    }
}
