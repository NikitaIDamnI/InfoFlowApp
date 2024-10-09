package com.example.infoflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import com.example.common.rememberThemeState
import com.example.common.saveThemeOnAppClose
import com.example.detail_news.DetailedNewsScreen
import com.example.infoflow.ui.theme.InfoFlowTheme
import com.example.navigation.AppNavGraph
import com.example.navigation.rememberNavigationState
import com.example.news_main.screen_contents.TestNewsMainScreen
import com.example.search.search_content_feature.TestSearchScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme = rememberThemeState()

            InfoFlowTheme(isDarkTheme.value) {
                val navigationState = rememberNavigationState()
                AppNavGraph(
                    navController = navigationState.navHostController,
                    mainScreenContent = {
                        TestNewsMainScreen(
                            isDarkTheme =isDarkTheme,
                            onClickNews = {
                                navigationState.navigationToDetailedNews(it)
                            },
                            onClickSetting = {
                                isDarkTheme.value = !isDarkTheme.value
                            },
                            onClickSearch = { category ->
                                navigationState.navigationToSearch(category, emptyList())
                            },
                            onClickNextAllNews = { category, content ->
                                navigationState.navigationToSearch(category, content)
                            }
                        )
                    },

                    searchScreenContent = {
                        TestSearchScreen(
                            categoryNews = it,
                            onClickNews = { navigationState.navigationToDetailedNews(it) },
                            onBackPressed = { navigationState.navHostController.popBackStack() }
                        )
                    },

                    detailedNewsScreenContent = {
                        DetailedNewsScreen(
                            onBackPressed = { navigationState.navHostController.popBackStack() },
                            onSettingPost = {},
                        )

                    },
                )

                DisposableEffect(Unit) {
                    onDispose {
                        saveThemeOnAppClose(context = applicationContext, isDarkTheme.value)
                    }
                }

            }

        }
    }

}

