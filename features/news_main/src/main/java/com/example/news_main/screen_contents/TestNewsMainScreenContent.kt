package com.example.news_main.screen_contents

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.common.ArticleUI
import com.example.common.CategoryNews
import com.example.common.IconTopBar
import com.example.common.MainBlueColor
import com.example.navigation.MainScreenNavGraph
import com.example.navigation.Screen
import com.example.navigation.rememberNavigationState
import com.example.news_main.NavigationItem
import com.example.news_main.R
import com.example.news_main.TestNewsMainScreenState
import com.example.news_main.TestNewsMainViewModel


@Composable
fun TestNewsMainScreen(
    onClickNews: (ArticleUI) -> Unit,
    onClickSearch: (CategoryNews) -> Unit,
    onClickSetting: () -> Unit,
    onClickNextAllNews: (CategoryNews,List<ArticleUI>) -> Unit
) {
    TestNewsMainScreen(
        viewModel = hiltViewModel(),
        onClickNews = onClickNews,
        onClickSearch = onClickSearch,
        onClickSetting = onClickSetting,
        onClickNextAllNews = onClickNextAllNews
    )
}

@Composable
internal fun TestNewsMainScreen(
    viewModel: TestNewsMainViewModel,
    onClickNews: (ArticleUI) -> Unit,
    onClickSearch: (CategoryNews) -> Unit,
    onClickSetting: () -> Unit,
    onClickNextAllNews: (CategoryNews,List<ArticleUI>) -> Unit

) {

    val state = viewModel.state.collectAsState()

    if (state.value.stateLoaded !is TestNewsMainScreenState.TestStateLoaded.Initial &&
        state.value.topHeadlines.isNotEmpty()
    ) {
        MainScreen(
            viewModel = viewModel,
            onClickNews = onClickNews,
            onClickSearch = onClickSearch,
            onClickSetting = onClickSetting,
            onClickNextAllNews = onClickNextAllNews
        )
    } else {
        PreviewMainScreen()
    }
}

@Composable
private fun MainScreen(
    viewModel: TestNewsMainViewModel,
    onClickNews: (ArticleUI) -> Unit,
    onClickSearch: (CategoryNews) -> Unit,
    onClickSetting: () -> Unit,
    onClickNextAllNews: (CategoryNews,List<ArticleUI>) -> Unit
) {
    val navigationState = rememberNavigationState()

    Log.d("TestNewsMainScreen_Log", "MainScreen")

    val state = viewModel.state.collectAsState()

    val stateNavScreen = remember { mutableStateOf<Screen>(Screen.Home) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                onClickSearch = onClickSearch,
                onClickSetting = onClickSetting
            )
        },
        bottomBar = {
            BottomBar(stateNavScreen) {
                navigationState.navigationTo(it)
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { paddingValues ->
        MainScreenNavGraph(
            navController = navigationState.navHostController,
            homeScreenContent = {
                HomeScreen(
                    paddingValues = paddingValues,
                    state = state,
                    imageLoader = viewModel.imageLoader,
                    onClickNews = onClickNews,
                    onClickNextAllNews = { categoryNews, content ->
                        onClickNextAllNews(categoryNews, content)
                    }

                )
                stateNavScreen.value = Screen.Home
            },
            favoriteScreenContent = {
                stateNavScreen.value = Screen.Favorite
                FavoriteScreen(
                    paddingValues = paddingValues,
                    listFavorites = state.value.recommendations,
                    imageLoader = viewModel.imageLoader,
                    onClickNews = onClickNews,
                    onDeleteFavoriteNews = {}
                )

            },
            worldScreenContent = {
                stateNavScreen.value = Screen.World
                TestScreenWorld()
            }
        )
    }
}



@Composable
fun TestScreenFavorite(modifier: Modifier = Modifier) {
    Log.d("TestNewsMainScreen_Log", "TestScreenFavorite")

}

@Composable
fun TestScreenWorld(modifier: Modifier = Modifier) {
    Log.d("TestNewsMainScreen_Log", "TestScreenWorld")

}



@Composable
fun BottomBar(
    stateNavigationBar: State<Screen>,
    onNavigateClick: (Screen) -> Unit
) {
    Log.d("TestNewsMainScreen_Log", "BottomBar")


    NavigationBar(
        containerColor = Color.Transparent,
    ) {
        NavigationItem.Companion.getAll().forEach { navItem ->

            NavigationBarItem(
                selected = navItem.screen == stateNavigationBar.value,
                onClick = {
                    onNavigateClick(navItem.screen)
                },
                icon = {
                    if (navItem.screen == stateNavigationBar.value) {
                        IconNavBottom(navigationItem = navItem)
                    } else {
                        Icon(
                            modifier = Modifier
                                .size(30.dp),
                            imageVector = navItem.icon,
                            contentDescription = "",
                            tint = Color.Gray
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MainBlueColor,
                    selectedTextColor = Color.White,
                    indicatorColor = MainBlueColor,
                    unselectedIconColor = Color.Gray,
                )
            )
        }
    }

}

@Composable
fun IconNavBottom(navigationItem: NavigationItem) {


    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(35.dp),
            tint = Color.White,
            imageVector = navigationItem.icon,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = stringResource(id = navigationItem.titleResId),
            color = Color.White,
            fontSize = 12.sp

        )
    }
}

@Composable
fun PreviewMainScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center),
            painter = painterResource(id = R.drawable.full_logo),
            contentDescription = null,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onClickSearch: (CategoryNews) -> Unit,
    onClickSetting: () -> Unit,
) {
    Log.d("TestNewsMainScreen_Log", "TopBar")

    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.shor_logo),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
        },
        actions = {
            IconTopBar(
                icon = Icons.Default.Search,
                onClick = { onClickSearch(CategoryNews.ALL) }
            )
            Spacer(modifier = Modifier.width(10.dp))
            IconTopBar(
                onClick = onClickSetting,
                icon = Icons.Default.Settings
            )
            Spacer(modifier = Modifier.width(10.dp))

        }
    )

}






