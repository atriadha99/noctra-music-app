package com.atriadha99.noctra.domain.source

import com.atriadha99.noctra.domain.model.Playlist
import com.atriadha99.noctra.domain.model.Track

interface MusicSource {
    val sourceId: String
    val capabilities: SourceCapabilities
    
    suspend fun search(query: String): List<Track>
    suspend fun getPlaylist(id: String): Playlist
    
    fun play(track: Track)
    fun pause()
    fun seekTo(positionMs: Long)
}
