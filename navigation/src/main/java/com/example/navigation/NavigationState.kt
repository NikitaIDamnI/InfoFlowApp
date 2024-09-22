package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.common.ArticleUI
import com.example.common.Screen

class NavigationState(
    val navHostController: NavHostController
) {
    fun navigationTo(route: String) {
        navHostController.navigate(route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true

        }
    }
    fun navigationToSearch(category: String){
        navHostController.navigate(Screen.Search.getRouteWithArgs(category))
    }
    fun navigationToDetailedNews(article: ArticleUI){
        navHostController.navigate(Screen.DetailedNews.getRouteWithArgs(article))
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController?= null
): NavigationState {
    val controller = navHostController ?: rememberNavController()
    return remember { NavigationState(controller) }
}