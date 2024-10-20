package com.example.infoflow

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import com.example.detailNews.DetailedNewsScreen
import com.example.navigation.AppNavGraph
import com.example.navigation.rememberNavigationState
import com.example.news_main.screen_contents.TestNewsMainScreen
import com.example.search.search_content_feature.TestSearchScreen
import com.example.uikit.InfoFlowTheme
import com.example.uikit.rememberThemeState
import com.example.uikit.saveThemeOnAppClose
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme = rememberThemeState()
            InfoFlowTheme(darkTheme = isDarkTheme.value) {
                val navigationState = rememberNavigationState()

                setStatusBarIconsColor(isDarkTheme.value)

                AppNavGraph(
                    navController = navigationState.navHostController,
                    mainScreenContent = {
                        TestNewsMainScreen(
                            isDarkTheme = isDarkTheme,
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
                            onBackPress = { navigationState.navHostController.popBackStack() },
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

    private fun setStatusBarIconsColor(isDarkTheme: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                if (isDarkTheme) {
                    controller.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    controller.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = if (isDarkTheme) {
                window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}
