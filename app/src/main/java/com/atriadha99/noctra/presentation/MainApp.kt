package com.atriadha99.noctra.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atriadha99.noctra.presentation.screens.HomeScreen
import com.atriadha99.noctra.presentation.screens.LibraryScreen
import com.atriadha99.noctra.presentation.screens.ExploreScreen
import com.atriadha99.noctra.presentation.screens.SettingsScreen

@Composable
fun MainApp(
    viewModel: com.atriadha99.noctra.ui.main.MainViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val navController = rememberNavController()
    
    // Automatically load local music when MainApp starts
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.loadLocalMusic()
    }
    
    val screens = listOf("Home", "Explore", "Library", "Settings")
    val icons = listOf(Icons.Filled.List, Icons.Filled.Search, Icons.Filled.List, Icons.Filled.Settings)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                screens.forEachIndexed { index, screen ->
                    val icon = if (index == 0) Icons.Filled.Home else icons[index]
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = screen) },
                        label = { Text(screen) },
                        selected = currentRoute == screen,
                        onClick = {
                            navController.navigate(screen) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            NavHost(navController, startDestination = "Home") {
                composable("Home") { HomeScreen(viewModel = viewModel) }
                composable("Explore") { ExploreScreen() }
                composable("Library") { LibraryScreen() }
                composable("Settings") { SettingsScreen(viewModel = viewModel) }
                composable("NowPlaying") { 
                    com.atriadha99.noctra.presentation.screens.NowPlayingScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    ) 
                }
            }
            
            // Floating MiniPlayer above everything except full screen NowPlaying
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute != "NowPlaying") {
                com.atriadha99.noctra.presentation.components.MiniPlayer(
                    viewModel = viewModel,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onNavigateToNowPlaying = { navController.navigate("NowPlaying") }
                )
            }
        }
    }
}
