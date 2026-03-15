package com.ffmpeg.engine.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Encode : Screen(
        route = "encode",
        title = "Encode",
        icon = Icons.Default.Movie
    )
    
    data object Presets : Screen(
        route = "presets",
        title = "Presets",
        icon = Icons.Default.Tune
    )
    
    data object History : Screen(
        route = "history",
        title = "History",
        icon = Icons.Default.History
    )
    
    data object Settings : Screen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}

val bottomNavItems = listOf(
    Screen.Encode,
    Screen.Presets,
    Screen.History,
    Screen.Settings
)
