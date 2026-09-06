package com.atriadha99.noctra.data.playback

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _currentTrackTitle = MutableStateFlow<String?>(null)
    val currentTrackTitle: StateFlow<String?> = _currentTrackTitle.asStateFlow()
    
    private val _currentTrackArtist = MutableStateFlow<String?>(null)
    val currentTrackArtist: StateFlow<String?> = _currentTrackArtist.asStateFlow()

    init {
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        
        controllerFuture?.addListener(
            {
                mediaController = controllerFuture?.get()
                setupController()
            },
            androidx.core.content.ContextCompat.getMainExecutor(context)
        )
    }

    private fun setupController() {
        val controller = mediaController ?: return
        
        controller.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _currentTrackTitle.value = mediaItem?.mediaMetadata?.title?.toString()
                _currentTrackArtist.value = mediaItem?.mediaMetadata?.artist?.toString()
                _duration.value = controller.duration.coerceAtLeast(0L)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _duration.value = controller.duration.coerceAtLeast(0L)
                }
            }
        })
    }
    
    fun updateProgress() {
        mediaController?.let {
            _currentPosition.value = it.currentPosition.coerceAtLeast(0L)
        }
    }

    fun play() {
        mediaController?.play()
    }

    fun pause() {
        mediaController?.pause()
    }

    fun skipToNext() {
        mediaController?.seekToNextMediaItem()
    }

    fun skipToPrevious() {
        mediaController?.seekToPreviousMediaItem()
    }

    fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }
    
    fun playMediaItem(mediaItem: MediaItem) {
        mediaController?.setMediaItem(mediaItem)
        mediaController?.prepare()
        mediaController?.play()
    }
    
    fun playMediaItems(mediaItems: List<MediaItem>, startIndex: Int = 0) {
        mediaController?.setMediaItems(mediaItems, startIndex, 0L)
        mediaController?.prepare()
        mediaController?.play()
    }

    fun release() {
        controllerFuture?.let { MediaController.releaseFuture(it) }
    }
}
