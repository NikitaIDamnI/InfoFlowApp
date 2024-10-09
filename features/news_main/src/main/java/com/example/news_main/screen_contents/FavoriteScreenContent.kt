package com.example.news_main.screen_contents

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.common.models.ArticleUI
import com.example.common.ContentListItem
import com.example.common.Title

@Composable
fun FavoriteScreen(
    paddingValues: PaddingValues,
    listFavorites: List<ArticleUI>,
    imageLoader: ImageLoader,
    onClickNews: (ArticleUI) -> Unit,
    onDeleteFavoriteNews: (ArticleUI) -> Unit,
) {
    val listState = rememberLazyListState()
    Log.d("Recomposition", "FavoriteScreen")
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            Title(
                modifier = Modifier
                    .fillMaxWidth(),
                mainTitle = "Favorites",
                commentForTitle = "Your favorite news from around the world"
            )
            Spacer(modifier = Modifier.height(10.dp))

            ListContent(
                modifier = Modifier
                    .fillMaxSize(),
                listFavorites = listFavorites,
                listState = listState,
                imageLoader = imageLoader,
                onClickItem = {
                    onClickNews(it)
                },
                onDeleteItem = onDeleteFavoriteNews
            )
        }
    }
}

@Composable
fun ListContent(
    modifier: Modifier,
    listFavorites: List<ArticleUI>,
    listState: LazyListState,
    imageLoader: ImageLoader,
    onClickItem: (ArticleUI) -> Unit,
    onDeleteItem: (ArticleUI) -> Unit,

    ) {

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(
            items = listFavorites,
            key = { it.url }
        ) {
            ContentListItem(it, imageLoader) { article ->
                onClickItem(article)
            }
        }
    }


}