package com.example.news_main.test.rosov

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import  androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun NewsMainScreen() {
    NewsMainScreen(viewModel())
}

@Composable
internal fun NewsMainScreen(viewModel: NewsMainViewModel) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is State.Success -> Articles(currentState)
        is State.Error -> {
        }
        is State.Loading -> {}
        State.None -> {}
    }
}

@Composable
fun Articles(state: State.Success, modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier

    ) {
//        items(stata.s) { article ->
//            key(article.id) {
//                ArticleItem(article)
//            }
//
//        }
    }

}

@Composable
fun ArticleItem(article: com.example.common.ArticleUI) {
    Box(
     modifier = Modifier
         .fillMaxSize()
         .background(Color.White)
    ){
        Text(text = article.title, style = MaterialTheme.typography.headlineMedium, maxLines = 1)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = article.description, style = MaterialTheme.typography.headlineMedium, maxLines = 3)
    }


}

