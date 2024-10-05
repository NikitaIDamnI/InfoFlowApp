package com.example.news_main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Language

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.navigation.Screen

sealed class NavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector,
) {
    data object Home : NavigationItem(
        screen = Screen.Home,
        titleResId = R.string.home,
        icon = Icons.Outlined.Home
    )

    data object Favorite : NavigationItem(
        screen = Screen.Favorite,
        titleResId = R.string.favorite,
        icon = Icons.Outlined.BookmarkBorder

    )

    data object World : NavigationItem(
        screen = Screen.World,
        titleResId = R.string.world,
        icon = Icons.Outlined.Language
    )

    companion object{
        fun getAll():List<NavigationItem>{
            return listOf(Home,Favorite,World)
        }
    }

}



