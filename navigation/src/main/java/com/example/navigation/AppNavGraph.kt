package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews

@Composable
fun AppNavGraph(
    navController: NavHostController,
    mainScreenContent: @Composable () -> Unit,
    searchScreenContent: @Composable (CategoryNews) -> Unit,
    detailedNewsScreenContent: @Composable (ArticleUI) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main
    ) {
        composable<Screen.Main> {
            mainScreenContent()
        }
        composable<Screen.Search>(
            typeMap = Screen.Search.typeMap
        ) {
            val category = it.toRoute<Screen.Search>().category
            searchScreenContent(
                category
            )
        }

        composable<Screen.DetailedNews>(
            typeMap = Screen.DetailedNews.typeMap
        ) {
            val newsUI = it.toRoute<Screen.DetailedNews>().articleUI
            detailedNewsScreenContent(
                newsUI
            )
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
        startDestination = Screen.Home
    ) {
        composable<Screen.Home> { homeScreenContent() }

        composable<Screen.Favorite> { favoriteScreenContent() }
        composable<Screen.World> { worldScreenContent() }
    }

}