package com.example.news_main.test.test_main_screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.ImageLoader
import coil.request.CachePolicy
import com.example.common.ArticleUI
import com.example.common.CategoryNews
import com.example.common.ContentListItem
import com.example.news_main.R
import com.example.common.GradientCard
import com.example.common.IconTopBar
import com.example.common.ImageNews
import com.example.common.MainBlueColor
import com.example.common.getTimeAgo
import com.example.navigation.NavigationState
import com.example.navigation.navGraph.MainScreenNavGraph
import com.example.navigation.rememberNavigationState


@Composable
fun TestNewsMainScreen(
    onClickNews: (ArticleUI) -> Unit,
    onClickSearch: (String) -> Unit,
    onClickSetting: () -> Unit,

    ) {
    TestNewsMainScreen(
        viewModel = hiltViewModel(),
        onClickNews = onClickNews,
        onClickSearch = onClickSearch,
        onClickSetting = onClickSetting,

        )
}

@Composable
internal fun TestNewsMainScreen(
    viewModel: TestNewsMainViewModel,
    onClickNews: (ArticleUI) -> Unit,
    onClickSearch: (String) -> Unit,
    onClickSetting: () -> Unit,
) {


    val state = viewModel.state.collectAsState()

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    if (state.value.stateLoaded !is TestNewsMainScreenState.TestStateLoaded.Initial &&
        state.value.topHeadlines.isNotEmpty()
    ) {
        MainScreen(
            state = state,
            imageLoader = imageLoader,
            onClickNews = onClickNews,
            onClickSearch = onClickSearch,
            onClickSetting = onClickSetting,
        )
    } else {
        PreviewMainScreen()
    }
}

@Composable
private fun MainScreen(
    state: State<TestNewsMainScreenState>,
    imageLoader: ImageLoader,
    onClickNews: (ArticleUI) -> Unit,
    onClickSearch: (String) -> Unit,
    onClickSetting: () -> Unit,
) {
    val navController = rememberNavigationState()

    val currentRoute = navController.getCurrentRoute()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                onClickSearch = onClickSearch,
                onClickSetting = onClickSetting
            )
        },
        bottomBar = {
            BottomBar(currentRoute) {
                navController.navigationTo(it)
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { paddingValues ->
        MainScreenNavGraph(
            navController = navController.navHostController,
            homeScreenContent = {
                HomeScreen(paddingValues, state, imageLoader, onClickNews)
            },
            favoriteScreenContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = currentRoute
                    )
                }
            },
            worldScreenContent = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Green)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = currentRoute
                    )
                }
            }
        )
    }
}


@Composable
private fun HomeScreen(
    paddingValues: PaddingValues,
    state: State<TestNewsMainScreenState>,
    imageLoader: ImageLoader,
    onClickNews: (ArticleUI) -> Unit
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        TopHeadlines(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .width(300.dp),
            state = state,
            imageLoader = imageLoader,
            onClickNews = { onClickNews(it) },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Recommendation(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            state = state,
            imageLoader = imageLoader,
            onClickNews = { onClickNews(it) }
        )
    }
}

@Composable
fun BottomBar(
    navState: String?,
    onClick: (String) -> Unit
) {

    val items =
        listOf(
            NavigationItem.Home,
            NavigationItem.World,
            NavigationItem.Favorite,
        )


    NavigationBar(
        containerColor = Color.Transparent,
    ) {
        items.forEach { navItem ->

            NavigationBarItem(
                selected = navItem.screen.route == navState,
                onClick = { onClick(navItem.screen.route) },
                icon = {
                    if (navItem.screen.route == navState) {
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
    onClickSearch: (String) -> Unit,
    onClickSetting: () -> Unit,
) {
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
                onClick = { onClickSearch(CategoryNews.ALL.toString()) }
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopHeadlines(
    modifier: Modifier = Modifier,
    state: State<TestNewsMainScreenState>,
    imageLoader: ImageLoader,
    onClickNews: (ArticleUI) -> Unit,

    ) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 5 }
    )
    Column(
        modifier = modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        ) {
            Text(
                modifier = Modifier,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                text = "Top Headlines"
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .padding(end = 10.dp),
                fontSize = 16.sp,
                color = MainBlueColor,
                text = "View all"
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            state = pagerState,
            contentPadding = PaddingValues(start = 30.dp, end = 30.dp),
        ) { page ->

            val scale by animateFloatAsState(
                targetValue = if (page == pagerState.currentPage) 1f else 0.9f
            )
            TopHeadlinesItem(
                modifier = Modifier
                    .scale(scale)
                    .clip(RoundedCornerShape(20.dp)),
                article = state.value.topHeadlines[page],
                imageLoader = imageLoader,
                onClickNews = onClickNews
            )

        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { pageIndex ->
                val color = if (pagerState.currentPage == pageIndex) MainBlueColor else Color.Gray
                if (pagerState.currentPage == pageIndex) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .height(7.dp)
                            .clip(CircleShape)
                            .width(20.dp)
                            .background(color)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }

            }
        }
    }

}


@Composable
fun TopHeadlinesItem(
    modifier: Modifier = Modifier,
    article: ArticleUI,
    imageLoader: ImageLoader,
    onClickNews: (ArticleUI) -> Unit,

    ) {
    Box(
        modifier = modifier
            .background(GradientCard)
            .clickable { onClickNews(article) }
    ) {
        ImageNews(
            modifier = Modifier.fillMaxSize(),
            url = article.imageUrl,
            imageLoader = imageLoader,
        )
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = article.title,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(5.dp))
            AuthorAndDataPublication(article)

        }
    }

}

@Composable
private fun AuthorAndDataPublication(article: com.example.common.ArticleUI) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically


    ) {
        Text(
            text = article.author,
            color = Color.White
        )
        Icon(
            modifier = Modifier
                .size(16.dp)
                .padding(start = 5.dp, end = 5.dp),
            imageVector = Icons.Default.Circle,
            tint = Color.White,
            contentDescription = null
        )
        Text(
            text = article.publishedAt?.getTimeAgo() ?: "",
            color = Color.White
        )
    }
}


@Composable
private fun Recommendation(
    modifier: Modifier = Modifier,
    state: State<TestNewsMainScreenState>,
    imageLoader: ImageLoader,
    onClickNews: (ArticleUI) -> Unit,
) {
    val recommendationsPreviewItems = state.value.recommendations.take(3)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        ) {
            Text(
                modifier = Modifier,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                text = "Recommendation"
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .padding(end = 10.dp),
                fontSize = 16.sp,
                color = MainBlueColor,
                text = "View all"
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f),
        ) {
            items(
                items = recommendationsPreviewItems,
                key = { it.url },
            ) {
                ContentListItem(
                    articles = it,
                    imageLoader = imageLoader,
                    onClick = {
                        onClickNews(it)
                    }
                )

            }
        }


    }
}

