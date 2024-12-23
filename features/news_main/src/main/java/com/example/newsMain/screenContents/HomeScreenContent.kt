package com.example.newsMain.screenContents

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import com.example.common.getTimeAgo
import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews
import com.example.newsMain.NewsMainScreenState

private const val SIZE_RECOMMENDATION = 3

@Composable
fun HomeScreen(
    state: State<NewsMainScreenState>,
    imageLoader: ImageLoader,
    onClickNews: (ArticleUI) -> Unit,
    modifier: Modifier = Modifier,
    onClickNextAllNews: (CategoryNews, List<ArticleUI>) -> Unit

) {
    Column(modifier = modifier) {
        TopHeadlines(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .width(300.dp),
            state = state,
            imageLoader = imageLoader,
            onClickNews = { onClickNews(it) },
            onClickNextAllNews = {
                onClickNextAllNews(
                    CategoryNews.TOP_HEADLINES,
                    state.value.topHeadlines,
                )
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Recommendation(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            state = state,
            imageLoader = imageLoader,
            onClickNews = { onClickNews(it) },
            onClickNextAllNews = {
                onClickNextAllNews(
                    CategoryNews.RECOMMENDATION,
                    state.value.recommendations
                )
            }
        )
    }
}

@Composable
@Suppress("LongMethod")
private fun TopHeadlines(
    state: State<NewsMainScreenState>,
    imageLoader: ImageLoader,
    onClickNews: (ArticleUI) -> Unit,
    modifier: Modifier = Modifier,
    onClickNextAllNews: () -> Unit

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
                    .padding(end = 10.dp)
                    .clickable { onClickNextAllNews() },
                fontSize = 16.sp,
                color = com.example.uikit.MainBlueColor,
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
                targetValue = if (page == pagerState.currentPage) 1f else 0.9f,
                label = ""
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
                val color =
                    if (pagerState.currentPage == pageIndex) com.example.uikit.MainBlueColor else Color.Gray
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
private fun TopHeadlinesItem(
    article: ArticleUI,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    onClickNews: (ArticleUI) -> Unit
) {
    Box(
        modifier = modifier
            .background(com.example.uikit.GradientCard)
            .clickable { onClickNews(article) }
    ) {
        com.example.uikit.ImageNews(
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
private fun AuthorAndDataPublication(article: ArticleUI) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = article.author,
            color = Color.White,
            maxLines = 1, // Ограничить текст одной строкой
            overflow = TextOverflow.Ellipsis, // Устанавливает многоточие в конце текста, если он не помещается
            modifier = Modifier
                .widthIn(
                    max = (LocalConfiguration.current.screenWidthDp.dp / 2)
                ) // Ограничивает ширину автора до половины
                .padding(end = 8.dp) // Небольшой отступ для красоты
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
            text = article.publishedAt.getTimeAgo(),
            color = Color.White
        )
    }
}

@Composable
private fun Recommendation(
    state: State<NewsMainScreenState>,
    imageLoader: ImageLoader,
    onClickNews: (ArticleUI) -> Unit,
    modifier: Modifier = Modifier,
    onClickNextAllNews: () -> Unit
) {
    val recommendation = remember {
        state.value.recommendations.take(SIZE_RECOMMENDATION)
    }
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
                    .padding(end = 10.dp)
                    .clickable { onClickNextAllNews() },
                fontSize = 16.sp,
                color = com.example.uikit.MainBlueColor,
                text = "View all"
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f),
        ) {
            items(
                items = recommendation,
                key = { it.url },
            ) {
                com.example.uikit.ContentListItem(
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
