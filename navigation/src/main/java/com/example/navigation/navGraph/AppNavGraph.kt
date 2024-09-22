package com.example.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.common.ArticleUI
import com.example.common.Screen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    newsMainScreenContent: @Composable () -> Unit,
    favoriteScreenContent: @Composable () -> Unit,
    searchScreenContent: @Composable (String) -> Unit,
    worldScreenContent: @Composable () -> Unit,
    detailedNewsScreenContent: @Composable (ArticleUI) -> Unit,
){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
       homeScreenNavGraph(
           mainNewsScreenScreenContent = newsMainScreenContent,
           searchScreenContent = searchScreenContent,
           detailedNewsScreenContent = detailedNewsScreenContent
       )
        composable(
            route = Screen.Favorite.route,
        ) { favoriteScreenContent() }
        composable(
            route = Screen.World.route,
        ) { worldScreenContent() }
    }

}