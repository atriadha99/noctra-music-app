package com.atriadha99.noctra.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.atriadha99.noctra.presentation.theme.LogoOrange
import com.atriadha99.noctra.ui.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    viewModel: MainViewModel? = null,
    onNavigateBack: (() -> Unit)? = null
) {
    val isPlaying by viewModel?.isPlaying?.collectAsState(initial = false) ?: remember { mutableStateOf(false) }
    val currentPosition by viewModel?.currentPosition?.collectAsState(initial = 0L) ?: remember { mutableStateOf(0L) }
    val duration by viewModel?.duration?.collectAsState(initial = 0L) ?: remember { mutableStateOf(0L) }
    val currentTitle by viewModel?.currentTrackTitle?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }
    val currentArtist by viewModel?.currentTrackArtist?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }

    val progress = if (duration > 0) (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f) else 0f
    
    var volumeLevel by remember { mutableFloatStateOf(0.7f) }
    var isShuffleOn by remember { mutableStateOf(false) }
    var repeatMode by remember { mutableIntStateOf(0) } // 0=off, 1=all, 2=one
    var showContextMenu by remember { mutableStateOf(false) }
    var showLyrics by remember { mutableStateOf(false) }

    fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    val dynamicGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4A2B20), // Simulated dominant color
            Color(0xFF1A1018),
            Color(0xFF0A0A0F)
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            // Top Bar: Back + Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onNavigateBack?.invoke() }) {
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "SEDANG DIPUTAR",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
                IconButton(onClick = { showContextMenu = true }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Album Art
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF2A2A30))
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            // Title & Like
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentTitle ?: "NOCTRA Anthem",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = currentArtist ?: "Andika Triadha",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = { /* TODO: Like */ }) {
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        contentDescription = "Like",
                        tint = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Quality Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    Icons.Filled.Headphones,
                    contentDescription = null,
                    tint = LogoOrange,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Hi-Quality",
                    style = MaterialTheme.typography.labelSmall,
                    color = LogoOrange
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            Slider(
                value = progress,
                onValueChange = { newProgress ->
                    val seekPos = (newProgress * duration).toLong()
                    viewModel?.seekTo(seekPos)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    formatTime(currentPosition),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
                Text(
                    "-${formatTime((duration - currentPosition).coerceAtLeast(0))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel?.skipToPrevious() }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        Icons.Filled.SkipPrevious,
                        contentDescription = "Previous",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                
                // Play/Pause button (large)
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { viewModel?.playPause() }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.Black,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                
                IconButton(onClick = { viewModel?.skipToNext() }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        Icons.Filled.SkipNext,
                        contentDescription = "Next",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // Secondary Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isShuffleOn = !isShuffleOn }) {
                    Icon(
                        Icons.Filled.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (isShuffleOn) LogoOrange else Color.White.copy(alpha = 0.5f)
                    )
                }
                IconButton(onClick = { 
                    repeatMode = (repeatMode + 1) % 3
                }) {
                    Icon(
                        imageVector = if (repeatMode == 2) Icons.Filled.RepeatOne else Icons.Filled.Repeat,
                        contentDescription = "Repeat",
                        tint = if (repeatMode > 0) LogoOrange else Color.White.copy(alpha = 0.5f)
                    )
                }
                IconButton(onClick = { /* TODO: Queue */ }) {
                    Icon(
                        Icons.Filled.QueueMusic,
                        contentDescription = "Queue",
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                }
                IconButton(onClick = { /* TODO: Speed */ }) {
                    Icon(
                        Icons.Filled.Speed,
                        contentDescription = "Playback Speed",
                        tint = Color.White.copy(alpha = 0.5f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Volume Slider (inline)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.VolumeDown,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
                Slider(
                    value = volumeLevel,
                    onValueChange = { volumeLevel = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White.copy(alpha = 0.8f),
                        inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                    )
                )
                Icon(
                    Icons.Filled.VolumeUp,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Lyrics Bottom Sheet Drag Handle
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(72.dp)
                .background(
                    Color(0x33000000),
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(top = 8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Drag handle
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.4f))
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Lirik",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
    
    // Context Menu Bottom Sheet
    if (showContextMenu) {
        ModalBottomSheet(
            onDismissRequest = { showContextMenu = false },
            containerColor = Color(0xFF1A1A1F)
        ) {
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                Text(
                    text = currentTitle ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
                Text(
                    text = currentArtist ?: "Unknown",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 2.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.White.copy(alpha = 0.1f))
                
                ContextMenuItem(Icons.Filled.FavoriteBorder, "Tambah ke Musik Disukai") { showContextMenu = false }
                ContextMenuItem(Icons.Filled.PlaylistAdd, "Tambah ke playlist") { showContextMenu = false }
                ContextMenuItem(Icons.Filled.Download, "Download") { showContextMenu = false }
                ContextMenuItem(Icons.Filled.QueueMusic, "Putar selanjutnya") { showContextMenu = false }
                ContextMenuItem(Icons.Filled.AddToQueue, "Tambah ke antrean") { showContextMenu = false }
                ContextMenuItem(Icons.Filled.Album, "Buka album") { showContextMenu = false }
                ContextMenuItem(Icons.Filled.Person, "Buka artis") { showContextMenu = false }
            }
        }
    }
}

@Composable
fun ContextMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(text, color = Color.White) },
        leadingContent = {
            Icon(icon, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                Modifier.padding(0.dp)
            )
    )
}
