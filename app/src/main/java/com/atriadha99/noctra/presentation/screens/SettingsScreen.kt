package com.atriadha99.noctra.presentation.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.atriadha99.noctra.presentation.components.SectionHeader
import com.atriadha99.noctra.presentation.theme.GlassSurface
import com.atriadha99.noctra.presentation.theme.LogoOrange
import com.atriadha99.noctra.ui.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel? = null
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.entries.all { it.value }
        if (isGranted) {
            viewModel?.loadLocalMusic()
        }
    }
    
    var crossfadeValue by remember { mutableFloatStateOf(0f) }
    var skipSilence by remember { mutableStateOf(false) }
    var spatialAudio by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp, bottom = 160.dp)
    ) {
        // Header
        Text(
            text = "Pengaturan",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // --- PROFILE SECTION ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(LogoOrange),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "Andika Triadha",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "andika@noctra.app",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // --- ACCOUNT & INTEGRATIONS ---
        SettingsSection("Akun & Integrasi") {
            SettingsItem(
                icon = Icons.Filled.MusicNote,
                title = "Spotify",
                subtitle = "Tidak terhubung",
                badge = null,
                onClick = { /* TODO */ }
            )
            SettingsDivider()
            SettingsItem(
                icon = Icons.Filled.Apple,
                title = "Apple Music",
                subtitle = "Tidak terhubung",
                badge = null,
                onClick = { /* TODO */ }
            )
            SettingsDivider()
            SettingsItem(
                icon = Icons.Filled.PlayCircle,
                title = "YouTube Music",
                subtitle = "Tidak terhubung",
                badge = null,
                onClick = { /* TODO */ }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // --- PENYIMPANAN & IZIN ---
        SettingsSection("Penyimpanan & Izin") {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Audiotrack,
                        contentDescription = null,
                        tint = LogoOrange
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Sinkronisasi Musik Lokal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Berikan izin agar NOCTRA dapat memutar file musik di perangkat Anda.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val perms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO)
                            } else {
                                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                            permissionLauncher.launch(perms)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LogoOrange)
                    ) {
                        Text("Izinkan Akses", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        Text("Pengaturan HP", color = Color.White)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // --- KUALITAS AUDIO ---
        SettingsSection("Kualitas Audio") {
            SettingsItem(
                icon = Icons.Filled.Wifi,
                title = "Streaming via Wi-Fi",
                subtitle = "Tinggi (320kbps)",
                onClick = { /* TODO */ }
            )
            SettingsDivider()
            SettingsItem(
                icon = Icons.Filled.CellTower,
                title = "Streaming via Data",
                subtitle = "Normal (128kbps)",
                onClick = { /* TODO */ }
            )
            SettingsDivider()
            SettingsItem(
                icon = Icons.Filled.Download,
                title = "Kualitas Unduhan",
                subtitle = "Sangat Tinggi (Lossless)",
                onClick = { /* TODO */ }
            )
            SettingsDivider()
            SettingsItem(
                icon = Icons.Filled.FolderSpecial,
                title = "Wi-Fi only download",
                subtitle = "Aktif",
                onClick = { /* TODO */ }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // --- PLAYBACK ---
        SettingsSection("Pemutaran") {
            // Crossfade slider
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Crossfade",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Text(
                        if (crossfadeValue == 0f) "Mati" else "${crossfadeValue.toInt()}s",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LogoOrange
                    )
                }
                Slider(
                    value = crossfadeValue,
                    onValueChange = { crossfadeValue = it },
                    valueRange = 0f..12f,
                    steps = 11,
                    colors = SliderDefaults.colors(
                        thumbColor = LogoOrange,
                        activeTrackColor = LogoOrange
                    )
                )
            }
            SettingsDivider()
            
            // Skip silence toggle
            SettingsToggleItem(
                title = "Lewati keheningan",
                subtitle = "Otomatis lewati bagian senyap",
                checked = skipSilence,
                onCheckedChange = { skipSilence = it }
            )
            SettingsDivider()
            
            // Spatial audio toggle
            SettingsToggleItem(
                title = "Audio Spasial",
                subtitle = "Pengalaman audio 3D (Beta)",
                checked = spatialAudio,
                onCheckedChange = { spatialAudio = it }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // --- SUMBER MUSIK ---
        SettingsSection("Sumber Musik") {
            SettingsItem(
                icon = Icons.Filled.Storage,
                title = "Musik Lokal",
                subtitle = "Aktif",
                badge = "IN USE",
                onClick = { /* TODO */ }
            )
            SettingsDivider()
            SettingsItem(
                icon = Icons.Filled.MusicNote,
                title = "Spotify",
                subtitle = "Tidak terhubung",
                onClick = { /* TODO */ }
            )
            SettingsDivider()
            SettingsItem(
                icon = Icons.Filled.PlayCircle,
                title = "YouTube Music",
                subtitle = "Experimental",
                onClick = { /* TODO */ }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // --- TENTANG ---
        SettingsSection("Tentang") {
            SettingsItem(
                icon = Icons.Filled.Info,
                title = "Versi",
                subtitle = "1.0.0 (Build 1)",
                onClick = { }
            )
            SettingsDivider()
            SettingsItem(
                icon = Icons.Filled.Code,
                title = "GitHub",
                subtitle = "github.com/atriadha99",
                onClick = { /* TODO: Open browser */ }
            )
            SettingsDivider()
            SettingsItem(
                icon = Icons.Filled.DeleteSweep,
                title = "Bersihkan Cache",
                subtitle = "128 MB digunakan",
                onClick = { /* TODO */ }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// --- Reusable Settings Components ---

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(alpha = 0.5f),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GlassSurface)
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title, color = Color.White) },
        supportingContent = { Text(subtitle, color = Color.White.copy(alpha = 0.6f)) },
        leadingContent = {
            Icon(icon, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (badge != null) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LogoOrange.copy(alpha = 0.2f)
                    ) {
                        Text(
                            badge,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = LogoOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f)
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .then(
                Modifier.padding(0.dp) // keep compact
            )
    )
}

@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title, color = Color.White) },
        supportingContent = { Text(subtitle, color = Color.White.copy(alpha = 0.6f)) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = LogoOrange,
                    uncheckedThumbColor = Color.White.copy(alpha = 0.7f),
                    uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
                )
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}

@Composable
fun SettingsDivider() {
    Divider(
        color = Color.White.copy(alpha = 0.06f),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
