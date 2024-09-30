package com.example.infoflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil.ImageLoader
import com.example.detail_news.DetailedNewsScreen
import com.example.infoflow.ui.theme.InfoFlowTheme
import com.example.navigation.navGraph.AppNavGraph
import com.example.navigation.rememberNavigationState
import com.example.news_main.test.test_main_screen.TestNewsMainScreen
import com.example.search.search_content_feature.TestSearchScreen
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


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
                    mainScreenContent = {
                        TestNewsMainScreen(
                            onClickNews = {
                                navigationState.navigationToDetailedNews(it)
                            },
                            onClickSetting = {},
                            onClickSearch = { navigationState.navigationToSearch(it) }
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