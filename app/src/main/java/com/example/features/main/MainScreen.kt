package com.example.features.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.navigation.*
import com.example.features.files.FilesScreen
import com.example.features.history.HistoryScreen
import com.example.features.home.HomeScreen
import com.example.features.settings.SettingsScreen

@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onNavigateOuter: (Any) -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 0.dp,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home", fontWeight = FontWeight.Bold) },
                    selected = currentDestination?.hierarchy?.any { it.route == HomeRoute::class.qualifiedName } == true,
                    colors = colors,
                    onClick = {
                        navController.navigate(HomeRoute) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.History, contentDescription = "History") },
                    label = { Text("History", fontWeight = FontWeight.Bold) },
                    selected = currentDestination?.hierarchy?.any { it.route == HistoryRoute::class.qualifiedName } == true,
                    colors = colors,
                    onClick = {
                        navController.navigate(HistoryRoute) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Folder, contentDescription = "Files") },
                    label = { Text("Files", fontWeight = FontWeight.Bold) },
                    selected = currentDestination?.hierarchy?.any { it.route == DocumentsRoute::class.qualifiedName } == true,
                    colors = colors,
                    onClick = {
                        navController.navigate(DocumentsRoute) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("Settings", fontWeight = FontWeight.Bold) },
                    selected = currentDestination?.hierarchy?.any { it.route == SettingsRoute::class.qualifiedName } == true,
                    colors = colors,
                    onClick = {
                        navController.navigate(SettingsRoute) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<HomeRoute> {
                HomeScreen(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle,
                    onNavigate = onNavigateOuter
                )
            }
            composable<HistoryRoute> {
                HistoryScreen()
            }
            composable<DocumentsRoute> {
                FilesScreen()
            }
            composable<SettingsRoute> {
                SettingsScreen(isDarkTheme = isDarkTheme, onThemeToggle = onThemeToggle)
            }
        }
    }
}
