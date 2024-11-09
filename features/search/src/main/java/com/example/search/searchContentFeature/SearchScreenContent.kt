package com.example.search.searchContentFeature

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews
import com.example.search.R

@Composable
fun SearchScreen(
    categoryNews: CategoryNews,
    onClickNews: (ArticleUI) -> Unit,
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit
) {
    SearchScreen(
        currentCategoryNews = categoryNews,
        modifier = modifier,
        onBackPress = onBackPress,
        onClickNews = onClickNews,
        viewModel = hiltViewModel()
    )
}

@Composable
@Suppress("LongMethod")
internal fun SearchScreen(
    currentCategoryNews: CategoryNews,
    viewModel: SearchScreenViewModel,
    onClickNews: (ArticleUI) -> Unit,
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    val focusRequester = remember { FocusRequester() }

    val listState = rememberLazyListState()

    LaunchedEffect(state.value.stateLoaded) {
        if (state.value.stateLoaded == SearchScreenState.StateLoaded.Loading) {
            listState.scrollToItem(0)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopBar(onBackPress = onBackPress) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        com.example.uikit.Title(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding(), start = 15.dp),
            mainTitle = "Discover",
            commentForTitle = "News from all around the world"
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 15.dp,
                    end = 10.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            SearchBarNews(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                state = state,
                onQueryChange = viewModel::onQueryChange,
                onSearch = viewModel::onSearchNews,
            )
            Spacer(modifier = Modifier.height(5.dp))
            ListCategories(
                state = state,
                currentCategoryNews = currentCategoryNews,
                onClickCategory = { viewModel.onCategoryChange(it) }
            )
            Spacer(modifier = Modifier.height(15.dp))
            ListContent(
                modifier = Modifier
                    .fillMaxSize(),
                state = state,
                listState = listState,
                imageLoader = viewModel.imageLoader,
                onClickItem = {
                    onClickNews(it)
                }
            )
        }
    }
}

@Composable
fun ListCategories(
    state: State<SearchScreenState>,
    onClickCategory: (CategoryNews) -> Unit,
    currentCategoryNews: CategoryNews,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(
            items = CategoryNews.toListCategory(currentCategoryNews),
            key = { it.name },
        ) { category ->
            com.example.uikit.CategoryCard(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onClickCategory(category) },
                categoryNews = category,
                activeCategory = state.value.category
            )
        }
    }
}

@Composable
fun ListContent(
    state: State<SearchScreenState>,
    listState: LazyListState,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    onClickItem: (ArticleUI) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(
            items = state.value.searchResult,
            key = { it.url }
        ) {
            com.example.uikit.ContentListItem(it, imageLoader) { article ->
                onClickItem(article)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchBarNews(
    state: State<SearchScreenState>,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
) {
    SearchBar(
        modifier = modifier,
        query = state.value.query ?: "",
        onQueryChange = {
            onQueryChange(it)
        },
        onSearch = {
            onSearch(it)
        },
        active = false,
        onActiveChange = {},
        trailingIcon = {
            Row {
                if (state.value.stateLoaded == SearchScreenState.StateLoaded.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                            .align(Alignment.CenterVertically),
                        color = Color.Gray,
                        strokeCap = StrokeCap.Round,
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(end = 5.dp),
                    onClick = { }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.filter_icon),
                        tint = Color.Gray,
                        contentDescription = null
                    )
                }
            }
        },
        placeholder = {
            Row {
                Text(
                    text = "Search",
                    color = Color.Gray
                )
            }
        },
        leadingIcon = {
            IconButton(onClick = {
                onSearch(state.value.query ?: "")
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = Color.Gray,
                    contentDescription = null
                )
            }
        }
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBackPress: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            com.example.uikit.IconTopBar(
                modifier = Modifier.padding(start = 10.dp),
                icon = Icons.Outlined.ArrowBackIosNew,
                onClick = onBackPress
            )
        }
    )
}
