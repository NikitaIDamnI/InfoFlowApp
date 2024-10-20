package com.example.newsMain.screenContents

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.example.common.models.ArticleUI

@Composable
fun FavoriteScreen(
    listFavorites: List<ArticleUI>,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    onClickNews: (ArticleUI) -> Unit,
) {
    val listState = rememberLazyListState()
    Log.d("Recomposition", "FavoriteScreen")
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column {
            com.example.uikit.Title(
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
            )
        }
    }
}

@Composable
fun ListContent(
    listFavorites: List<ArticleUI>,
    listState: LazyListState,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    onClickItem: (ArticleUI) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(
            items = listFavorites,
            key = { it.url }
        ) {
            com.example.uikit.ContentListItem(it, imageLoader) { article ->
                onClickItem(article)
            }
        }
    }
}
