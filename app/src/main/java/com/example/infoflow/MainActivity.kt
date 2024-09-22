package com.example.infoflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.detail_news.DetailedNewsScreen
import com.example.infoflow.ui.theme.InfoFlowTheme
import com.example.navigation.navGraph.AppNavGraph
import com.example.navigation.rememberNavigationState
import com.example.news_main.test.test_main_screen.TestNewsMainScreen
import com.example.search.search_content_feature.TestSearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InfoFlowTheme {
                val navigationState = rememberNavigationState()
                AppNavGraph(
                    navController = navigationState.navHostController,
                    newsMainScreenContent = {
                        TestNewsMainScreen(
                            onClickNews = {
                                navigationState.navigationToDetailedNews(it)
                            },
                            onNavigation = { navigationState.navigationTo(it) },
                            onClickSetting = {},
                            onClickSearch = { navigationState.navigationToSearch(it) },
                        )
                    },
                    favoriteScreenContent = {
                        Text("FAVORITE SCREEN CONTENT")
                    },
                    searchScreenContent = {
                        TestSearchScreen(
                            categoryNews = it,
                            onClickNews = { navigationState.navigationToDetailedNews(it) },
                            onBackPressed = { navigationState.navHostController.popBackStack() }
                        )
                    },
                    worldScreenContent = {
                        Text("WORLD SCREEN CONTENT")
                    },
                    detailedNewsScreenContent = {
                        DetailedNewsScreen(
                            articleUI = it,
                            onBackPressed = { navigationState.navHostController.popBackStack() },
                            onAddFavorite = {},
                            onSettingPost = {},
                        )

                    },
                )
            }

        }
    }
}

@Preview
@Composable
fun GreetingPreview() {
    InfoFlowTheme {
        Text("HI")
    }
}