package com.atriadha99.noctra.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.atriadha99.noctra.presentation.components.AlbumCard
import com.atriadha99.noctra.presentation.components.ArtistCircle
import com.atriadha99.noctra.presentation.components.SectionHeader
import com.atriadha99.noctra.presentation.components.TrackListItem
import com.atriadha99.noctra.ui.main.MainViewModel

@Composable
fun ExploreScreen(
    viewModel: MainViewModel? = null
) {
    val localTracks by viewModel?.localTracks?.collectAsState(initial = emptyList()) ?: remember { mutableStateOf(emptyList()) }
    val currentTitle by viewModel?.currentTrackTitle?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }
    
    // Derive unique artists from local tracks
    val uniqueArtists = remember(localTracks) {
        localTracks.map { it.artist }.distinct().take(10)
    }
    
    // Group tracks as "albums" by artist
    val tracksByArtist = remember(localTracks) {
        localTracks.groupBy { it.artist }.entries.take(6)
    }
    
    // "Trending" — most recently found tracks
    val trendingTracks = remember(localTracks) {
        localTracks.take(10)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp, bottom = 160.dp)
    ) {
        // Header
        Text(
            text = "Jelajah",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Top Artists Section
        if (uniqueArtists.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionHeader("Artis populer")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uniqueArtists) { artist ->
                        ArtistCircle(
                            name = artist,
                            onClick = { /* TODO: Navigate to artist */ }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // New Albums (grouped by artist)
        if (tracksByArtist.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionHeader("Album & Single baru")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tracksByArtist.toList()) { (artist, tracks) ->
                        AlbumCard(
                            title = tracks.firstOrNull()?.title ?: "Unknown",
                            subtitle = artist,
                            onClick = {
                                viewModel?.playPlaylist(tracks)
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Trending Section (track list)
        if (trendingTracks.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                SectionHeader("Trending di perangkat")
            }
            
            trendingTracks.forEachIndexed { index, track ->
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
        
        // Empty state
        if (localTracks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text(
                        text = "Belum ada musik",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Berikan izin penyimpanan di Pengaturan untuk menemukan musik lokal Anda.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
