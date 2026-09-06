package com.atriadha99.noctra.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.atriadha99.noctra.presentation.components.AlbumCard
import com.atriadha99.noctra.presentation.components.GradientCard
import com.atriadha99.noctra.presentation.components.SectionHeader
import com.atriadha99.noctra.presentation.theme.LogoOrange
import com.atriadha99.noctra.presentation.theme.LogoOrangeDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: com.atriadha99.noctra.ui.main.MainViewModel? = null
) {
    val scrollState = rememberScrollState()
    
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Musik", "Podcast")
    
    val localTracks by viewModel?.localTracks?.collectAsState(initial = emptyList()) ?: remember { mutableStateOf(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 48.dp, bottom = 80.dp) // padding for top status bar and bottom nav
    ) {
        // Header & Avatar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Selamat Pagi",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filter Pills
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters.size) { index ->
                val filter = filters[index]
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // "Lanjutkan mendengarkan" Section
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionHeader("Lanjutkan mendengarkan")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(5) { index ->
                    AlbumCard(
                        title = "Mix $index",
                        subtitle = "Berdasarkan selera Anda",
                        onClick = { /* TODO */ }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // "Di perangkat" Section
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionHeader("Di perangkat")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    GradientCard(
                        title = "Musik Lokal (${localTracks.size} Lagu)",
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
                        gradientColors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2)),
                        onClick = { /* TODO */ }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // "Dibuat untukmu" Section
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionHeader("Dibuat untukmu")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(3) { index ->
                    AlbumCard(
                        title = "Daily Mix ${index + 1}",
                        subtitle = "NOCTRA Music",
                        onClick = { /* TODO */ }
                    )
                }
            }
        }
    }
}
