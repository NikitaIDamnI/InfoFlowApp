package com.example.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.common.ArticleUI
import com.example.common.Screen
import com.google.gson.Gson

@Composable
fun AppNavGraph(
    navController: NavHostController,
    mainScreenContent: @Composable () -> Unit,
    searchScreenContent: @Composable (String) -> Unit,
    detailedNewsScreenContent: @Composable (ArticleUI) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            mainScreenContent()
        }
        composable(
            route = Screen.Search.route,
        ) {
            val category = it.arguments?.getString(Screen.KEY_SEARCH) ?: ""
            searchScreenContent(
                category
            )
        }
        composable(route = Screen.DetailedNews.route) {
            val jsonFeedPost = it.arguments?.getString(Screen.KEY_ARTICLE) ?: ""
            val articleUI = Gson().fromJson(jsonFeedPost, ArticleUI::class.java)
            detailedNewsScreenContent(articleUI)
        }


    }

}

@Composable
fun MainScreenNavGraph(
    navController: NavHostController,
    homeScreenContent: @Composable () -> Unit,
    favoriteScreenContent: @Composable () -> Unit,
    worldScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route,
        ) { homeScreenContent() }

        composable(
            route = Screen.Favorite.route,
        ) { favoriteScreenContent() }
        composable(
            route = Screen.World.route,
        ) { worldScreenContent() }
    }

}