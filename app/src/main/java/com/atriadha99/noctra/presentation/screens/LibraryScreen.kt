package com.atriadha99.noctra.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.atriadha99.noctra.presentation.components.ArtistCircle
import com.atriadha99.noctra.presentation.components.GradientCard
import com.atriadha99.noctra.presentation.components.SectionHeader
import com.atriadha99.noctra.presentation.components.TrackListItem
import com.atriadha99.noctra.presentation.theme.LogoOrange
import com.atriadha99.noctra.presentation.theme.LogoOrangeDark
import com.atriadha99.noctra.ui.main.MainViewModel

@Composable
fun LibraryScreen(
    viewModel: MainViewModel? = null
) {
    val localTracks by viewModel?.localTracks?.collectAsState(initial = emptyList()) ?: remember { mutableStateOf(emptyList()) }
    val currentTitle by viewModel?.currentTrackTitle?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }

    val uniqueArtists = remember(localTracks) {
        localTracks.map { it.artist }.distinct().take(8)
    }
    
    val topTracks = remember(localTracks) {
        localTracks.take(5)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp, bottom = 160.dp)
    ) {
        // Header
        Text(
            text = "Koleksi",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Your Replay Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1A0A2E),
                            Color(0xFF3D1C6E),
                            LogoOrange
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Your Replay",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "${localTracks.size} lagu • ${uniqueArtists.size} artis",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // On Device Section
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionHeader("Di perangkat")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    GradientCard(
                        title = "Musik Lokal\n${localTracks.size} Lagu",
                        gradientColors = listOf(LogoOrange, LogoOrangeDark),
                        onClick = {
                            if (localTracks.isNotEmpty()) {
                                viewModel?.playPlaylist(localTracks)
                            }
                        }
                    )
                }
                item {
                    GradientCard(
                        title = "Unduhan",
                        gradientColors = listOf(Color(0xFF5B21B6), Color(0xFF7C3AED)),
                        onClick = { /* TODO */ }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Playlists Section
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionHeader("Playlist")
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // New Playlist button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "New Playlist",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Playlist Baru",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
            
            // Liked Music
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF7C3AED), Color(0xFFEC4899))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Liked Music",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "Musik Disukai",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        "0 lagu",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Artists Section
        if (uniqueArtists.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionHeader("Artis")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uniqueArtists) { artist ->
                        ArtistCircle(
                            name = artist,
                            onClick = { /* TODO */ }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Top Songs Section (from local tracks)
        if (topTracks.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionHeader("Lagu teratas Anda")
            }
            
            topTracks.forEachIndexed { index, track ->
                TrackListItem(
                    title = track.title,
                    artist = track.artist,
                    index = index + 1,
                    isPlaying = currentTitle == track.title,
                    onClick = { viewModel?.playPlaylist(localTracks, index) },
                    onMoreClick = { /* TODO */ }
                )
            }
        }
    }
}
