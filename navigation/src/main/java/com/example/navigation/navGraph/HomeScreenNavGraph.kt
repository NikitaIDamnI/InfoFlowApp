package com.example.navigation.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.common.ArticleUI
import com.example.common.Screen
import com.google.gson.Gson

fun NavGraphBuilder.homeScreenNavGraph(
    mainNewsScreenScreenContent: @Composable () -> Unit,
    searchScreenContent: @Composable (String) -> Unit,
    detailedNewsScreenContent: @Composable (ArticleUI) -> Unit
) {
    navigation(
        startDestination = Screen.MainNews.route,
        route = Screen.Home.route,
    ) {
        composable(Screen.MainNews.route) {
            mainNewsScreenScreenContent()
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
            val articleUI = Gson().fromJson(jsonFeedPost,ArticleUI::class.java)
            detailedNewsScreenContent(articleUI)
        }

    }

}