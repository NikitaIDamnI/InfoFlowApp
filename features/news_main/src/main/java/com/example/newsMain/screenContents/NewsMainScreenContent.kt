package com.example.newsMain.screenContents

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
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
import coil.ImageLoader
import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews
import com.example.navigation.MainScreenNavGraph
import com.example.navigation.Screen
import com.example.navigation.rememberNavigationState
import com.example.newsMain.NavigationItem
import com.example.newsMain.NewsMainScreenState
import com.example.newsMain.NewsMainViewModel
import com.example.news_main.R
import com.example.uikit.IconTopBar

const val ALPHA_ON_BACKGROUND = 0.1f

@Composable
fun NewsMainScreen(
    isDarkTheme: State<Boolean>,
    onClickNews: (ArticleUI) -> Unit,
    onClickSearch: (CategoryNews) -> Unit,
    onClickSetting: () -> Unit,
    onClickNextAllNews: (CategoryNews, List<ArticleUI>) -> Unit
) {
    NewsMainScreen(
        viewModel = hiltViewModel(),
        isDarkTheme = isDarkTheme,
        onClickNews = onClickNews,
        onClickSearch = onClickSearch,
        onClickSetting = onClickSetting,
        onClickNextAllNews = onClickNextAllNews
    )
}

@Composable
@Suppress("LongParameterList")
internal fun NewsMainScreen(
    viewModel: NewsMainViewModel,
    isDarkTheme: State<Boolean>,
    onClickNews: (ArticleUI) -> Unit,
    onClickSearch: (CategoryNews) -> Unit,
    onClickSetting: () -> Unit,
    modifier: Modifier = Modifier,
    onClickNextAllNews: (CategoryNews, List<ArticleUI>) -> Unit
) {
    val state = viewModel.state.collectAsState()

    if (state.value.stateLoaded !is NewsMainScreenState.TestStateLoaded.Initial &&
        state.value.topHeadlines.isNotEmpty()
    ) {
        MainScreen(
            state = state,
            isDarkTheme = isDarkTheme,
            imageLoader = viewModel.imageLoader,
            onClickNews = onClickNews,
            onClickSearch = onClickSearch,
            onClickSetting = onClickSetting,
            modifier = modifier,
            onClickNextAllNews = onClickNextAllNews
        )
    } else {
        PreviewMainScreen()
    }
}

@Composable
@Suppress("LongParameterList")
private fun MainScreen(
    state: State<NewsMainScreenState>,
    isDarkTheme: State<Boolean>,
    imageLoader: ImageLoader,
    onClickNews: (ArticleUI) -> Unit,
    onClickSearch: (CategoryNews) -> Unit,
    onClickSetting: () -> Unit,
    modifier: Modifier = Modifier,
    onClickNextAllNews: (CategoryNews, List<ArticleUI>) -> Unit
) {
    val navigationState = rememberNavigationState()
    val stateNavScreen = remember { mutableStateOf<Screen>(Screen.Home) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                isDarkTheme = isDarkTheme,
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
                    modifier = Modifier.padding(paddingValues),
                    state = state,
                    imageLoader = imageLoader,
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding()
                        ),
                    listFavorites = state.value.favorites,
                    imageLoader = imageLoader,
                    onClickNews = onClickNews,
                )
            },
            worldScreenContent = {
                stateNavScreen.value = Screen.World
            }
        )
    }
}

@Composable
fun BottomBar(
    stateNavigationBar: State<Screen>,
    modifier: Modifier = Modifier,
    onNavigateClick: (Screen) -> Unit
) {
    Log.d("Recomposition", "BottomBar")

    NavigationBar(
        modifier = modifier,
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
                    selectedIconColor = com.example.uikit.MainBlueColor,
                    selectedTextColor = Color.White,
                    indicatorColor = com.example.uikit.MainBlueColor,
                    unselectedIconColor = Color.Gray,
                )
            )
        }
    }
}

@Composable
fun IconNavBottom(
    navigationItem: NavigationItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
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
fun PreviewMainScreen(modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val backColor = if (isDarkTheme) {
        Color.Black
    } else {
        Color.White
    }
    val imageLogo = if (isDarkTheme) {
        R.drawable.full_logo_night
    } else {
        R.drawable.full_logo_day
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backColor)
    ) {
        Image(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center),
            painter = painterResource(id = imageLogo),
            contentDescription = null,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    isDarkTheme: State<Boolean>,
    onClickSearch: (CategoryNews) -> Unit,
    onClickSetting: () -> Unit,
) {
    val imageLogo = if (isDarkTheme.value) {
        R.drawable.shor_logo_night
    } else {
        R.drawable.shor_logo_day
    }

    val logoIconTheme = if (isDarkTheme.value) {
        Icons.Outlined.WbSunny
    } else {
        Icons.Default.Nightlight
    }

    TopAppBar(
        colors = TopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.Transparent,
            titleContentColor = Color.Transparent,
            actionIconContentColor = Color.Transparent
        ),
        title = {
            Image(
                painter = painterResource(id = imageLogo),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
        },
        actions = {
            IconTopBar(
                icon = Icons.Default.Search,
                onClick = { onClickSearch(CategoryNews.ALL) },
                colorBack = MaterialTheme.colorScheme.onBackground.copy(alpha = ALPHA_ON_BACKGROUND),
                colorIcon = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(10.dp))
            IconTopBar(
                onClick = onClickSetting,
                icon = logoIconTheme,
                colorBack = MaterialTheme.colorScheme.onBackground.copy(alpha = ALPHA_ON_BACKGROUND),
                colorIcon = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    )
}