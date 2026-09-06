package com.atriadha99.noctra.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atriadha99.noctra.presentation.components.MiniPlayer
import com.atriadha99.noctra.presentation.screens.*
import com.atriadha99.noctra.presentation.theme.BackgroundDark
import com.atriadha99.noctra.presentation.theme.GlassSurface
import com.atriadha99.noctra.presentation.theme.GlassBorder
import com.atriadha99.noctra.ui.main.MainViewModel

data class NavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun MainApp(
    viewModel: MainViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val navController = rememberNavController()
    
    LaunchedEffect(Unit) {
        viewModel.loadLocalMusic()
    }
    
    val navItems = listOf(
        NavItem("Home", "Home", Icons.Filled.Home),
        NavItem("Explore", "Explore", Icons.Filled.Search),
        NavItem("Library", "Library", Icons.Filled.LibraryMusic),
        NavItem("Settings", "Settings", Icons.Filled.Settings)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Main content area
        NavHost(
            navController,
            startDestination = "Home",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("Home") { HomeScreen(viewModel = viewModel) }
            composable("Explore") { ExploreScreen(viewModel = viewModel) }
            composable("Library") { LibraryScreen(viewModel = viewModel) }
            composable("Settings") { SettingsScreen(viewModel = viewModel) }
            composable("NowPlaying") {
                NowPlayingScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // Floating elements at the bottom
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        
        if (currentRoute != "NowPlaying") {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 12.dp)
            ) {
                // Floating MiniPlayer
                MiniPlayer(
                    viewModel = viewModel,
                    onNavigateToNowPlaying = { navController.navigate("NowPlaying") }
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Floating Pill Bottom Navigation
                FloatingBottomNav(
                    navItems = navItems,
                    currentRoute = currentRoute ?: "Home",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun FloatingBottomNav(
    navItems: List<NavItem>,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(GlassSurface)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = currentRoute == item.route
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent,
                    label = "navBg"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f),
                    label = "navContent"
                )
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(bgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onNavigate(item.route) }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = contentColor,
                            modifier = Modifier.size(22.dp)
                        )
                        if (isSelected) {
                            Text(
                                text = item.label,
                                color = contentColor,
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
