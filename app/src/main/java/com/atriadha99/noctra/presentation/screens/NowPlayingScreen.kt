package com.atriadha99.noctra.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    viewModel: com.atriadha99.noctra.ui.main.MainViewModel? = null,
    onNavigateBack: (() -> Unit)? = null // Made optional so preview works, usually injected via MainApp
) {
    val isPlaying by viewModel?.isPlaying?.collectAsState(initial = false) ?: androidx.compose.runtime.mutableStateOf(false)
    val currentPosition by viewModel?.currentPosition?.collectAsState(initial = 0L) ?: androidx.compose.runtime.mutableStateOf(0L)
    val duration by viewModel?.duration?.collectAsState(initial = 0L) ?: androidx.compose.runtime.mutableStateOf(0L)
    val currentTitle by viewModel?.currentTrackTitle?.collectAsState(initial = null) ?: androidx.compose.runtime.mutableStateOf(null)
    val currentArtist by viewModel?.currentTrackArtist?.collectAsState(initial = null) ?: androidx.compose.runtime.mutableStateOf(null)

    // Calculate progress as a float from 0 to 1
    val progress = if (duration > 0) (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f) else 0f
    
    // Format duration helper
    fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    // Placeholder for actual Haze blur effect
    val dynamicGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4A2B20), // Simulated dominant color from logo orange
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dynamicGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onNavigateBack?.invoke() }) {
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Minimize", tint = Color.White)
                }
                Text(
                    text = "Sekarang Memutar",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.7f)
                )
                IconButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Menu", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Album Art
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.DarkGray)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Title & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentTitle ?: "NOCTRA Anthem",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = currentArtist ?: "Andika Triadha",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = { /* TODO: Like */ }) {
                    Icon(Icons.Filled.FavoriteBorder, contentDescription = "Like", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Progress Bar
            Slider(
                value = progress,
                onValueChange = { newProgress -> 
                    val seekPos = (newProgress * duration).toLong()
                    viewModel?.seekTo(seekPos)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatTime(currentPosition), style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                Text(formatTime(duration), style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel?.skipToPrevious() }, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous", tint = Color.White, modifier = Modifier.size(36.dp))
                }
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { viewModel?.playPause() }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, 
                            contentDescription = "Play/Pause", 
                            tint = Color.White, 
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                IconButton(onClick = { viewModel?.skipToNext() }, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Filled.SkipNext, contentDescription = "Next", tint = Color.White, modifier = Modifier.size(36.dp))
                }
            }
        }
        
        // Lyrics Bottom Sheet Placeholder
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0x33000000), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Lirik",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
