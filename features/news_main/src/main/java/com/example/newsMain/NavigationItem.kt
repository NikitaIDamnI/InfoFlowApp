package com.example.newsMain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Language
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.navigation.Screen
import com.example.news_main.R

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector,
    val route: String?
) {
    data object Home : NavigationItem(
        screen = Screen.Home,
        titleResId = R.string.home,
        icon = Icons.Outlined.Home,
        route = Screen.Home::class.qualifiedName
    )

    data object Favorite : NavigationItem(
        screen = Screen.Favorite,
        titleResId = R.string.favorite,
        icon = Icons.Outlined.BookmarkBorder,
        route = Screen.Favorite::class.qualifiedName

    )

    data object World : NavigationItem(
        screen = Screen.World,
        titleResId = R.string.world,
        icon = Icons.Outlined.Language,
        route = Screen.World::class.qualifiedName
    )

    companion object {
        fun getAll(): List<NavigationItem> {
            return listOf(Home, Favorite, World)
        }
    }
}
