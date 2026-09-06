package com.atriadha99.noctra.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.atriadha99.noctra.data.playback.PlaybackManager
import com.atriadha99.noctra.data.source.local.LocalFileSourceAdapter
import com.atriadha99.noctra.domain.model.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playbackManager: PlaybackManager,
    private val localFileSourceAdapter: LocalFileSourceAdapter
) : ViewModel() {

    val isPlaying = playbackManager.isPlaying
    val currentPosition = playbackManager.currentPosition
    val duration = playbackManager.duration
    val currentTrackTitle = playbackManager.currentTrackTitle
    val currentTrackArtist = playbackManager.currentTrackArtist

    private val _localTracks = MutableStateFlow<List<Track>>(emptyList())
    val localTracks: StateFlow<List<Track>> = _localTracks.asStateFlow()

    init {
        // Start a loop to update progress while playing
        viewModelScope.launch {
            while (true) {
                if (isPlaying.value) {
                    playbackManager.updateProgress()
                }
                delay(1000)
            }
        }
    }

    fun loadLocalMusic() {
        viewModelScope.launch {
            _localTracks.value = localFileSourceAdapter.getAllLocalTracks()
        }
    }

    fun playTrack(track: Track) {
        val mediaItem = MediaItem.fromUri(track.streamUrl)
        val mediaItemWithMetadata = mediaItem.buildUpon()
            .setMediaId(track.id)
            .setMediaMetadata(
                androidx.media3.common.MediaMetadata.Builder()
                    .setTitle(track.title)
                    .setArtist(track.artist)
                    .setArtworkUri(android.net.Uri.parse(track.coverUrl ?: ""))
                    .build()
            )
            .build()
        playbackManager.playMediaItem(mediaItemWithMetadata)
    }
    
    fun playPlaylist(tracks: List<Track>, startIndex: Int = 0) {
        val mediaItems = tracks.map { track ->
            MediaItem.Builder()
                .setUri(track.streamUrl)
                .setMediaId(track.id)
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(track.artist)
                        .setArtworkUri(android.net.Uri.parse(track.coverUrl ?: ""))
                        .build()
                )
                .build()
        }
        playbackManager.playMediaItems(mediaItems, startIndex)
    }

    fun playPause() {
        if (isPlaying.value) {
            playbackManager.pause()
        } else {
            playbackManager.play()
        }
    }

    fun skipToNext() {
        playbackManager.skipToNext()
    }

    fun skipToPrevious() {
        playbackManager.skipToPrevious()
    }

    fun seekTo(position: Long) {
        playbackManager.seekTo(position)
    }
    
    override fun onCleared() {
        super.onCleared()
        playbackManager.release()
    }
}
