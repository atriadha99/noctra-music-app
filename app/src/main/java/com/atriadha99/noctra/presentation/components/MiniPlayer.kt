package com.atriadha99.noctra.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Column

@Composable
fun MiniPlayer(
    viewModel: com.atriadha99.noctra.ui.main.MainViewModel? = null,
    modifier: Modifier = Modifier,
    onNavigateToNowPlaying: () -> Unit
) {
    val isPlaying by viewModel?.isPlaying?.collectAsState(initial = false) ?: androidx.compose.runtime.mutableStateOf(false)
    val currentTitle by viewModel?.currentTrackTitle?.collectAsState(initial = null) ?: androidx.compose.runtime.mutableStateOf(null)
    val currentArtist by viewModel?.currentTrackArtist?.collectAsState(initial = null) ?: androidx.compose.runtime.mutableStateOf(null)

    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .height(64.dp)
            .clickable { onNavigateToNowPlaying() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for Album Art
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.DarkGray)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Track Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = currentTitle ?: "Not Playing",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentArtist ?: "NOCTRA",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Controls
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel?.playPause() }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { viewModel?.skipToNext() }) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
