package com.example.detail_news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import coil.ImageLoader
import coil.request.CachePolicy
import com.example.common.IconTopBar
import com.example.common.ImageNews

@Composable
fun DetailedNewsScreen(
    navBackStackEntry: NavBackStackEntry,
    onBackPressed: () -> Unit,
    onAddFavorite: () -> Unit,
    onSettingPost: () -> Unit
) {
    DetailedNewsScreen(
        viewModel = hiltViewModel(navBackStackEntry),
        onBackPressed = onBackPressed,
        onAddFavorite = onAddFavorite,
        onSettingPost = onSettingPost
    )
}

@Composable
internal fun DetailedNewsScreen(
    viewModel: DetailedNewsScreenViewModel,
    onBackPressed: () -> Unit,
    onAddFavorite: () -> Unit,
    onSettingPost: () -> Unit
) {

    val state = viewModel.state.collectAsState()

    val articleUI = state.value.article

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()


    ImageNews(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.55f),
        imageLoader = imageLoader,
        url = articleUI.imageUrl,
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.55f)
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(Color.Transparent.copy(0.05f))
                .align(Alignment.BottomStart)
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = articleUI.title,
                    fontSize = 25.sp,
                    color = Color.White

                )
                Text(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    text = articleUI.publishedAt.toString(),
                    fontSize = 15.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(60.dp))

            }

        }

    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { TopBar(
            onBackPressed = onBackPressed,
            onAddFavorite = onAddFavorite,
            onSettingPost = onSettingPost
        ) },
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { paddingValues ->
        paddingValues
        Column {
            Spacer(Modifier.fillMaxHeight(0.49f))

            Card(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    text = articleUI.content
                )
            }
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBackPressed: () -> Unit,
    onAddFavorite: () -> Unit,
    onSettingPost: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            titleContentColor = Color.Black
        ),
        title = {
            Spacer(modifier = Modifier.fillMaxWidth()) // Добавляем Spacer для выравнивания

        },
        navigationIcon = {
            IconTopBar(
                icon = Icons.Default.ArrowBackIosNew,
                colorIcon = Color.White,
                colorBack = Color.Transparent.copy(0.1f),
                onClick = onBackPressed

            )
        },
        actions = {
            IconTopBar(
                icon = Icons.Outlined.BookmarkBorder,
                colorIcon = Color.White,
                colorBack = Color.Transparent.copy(0.1f),
                onClick = onAddFavorite

            )
            Spacer(modifier = Modifier.width(10.dp))
            IconTopBar(
                icon = Icons.Default.MoreHoriz,
                colorIcon = Color.White,
                colorBack = Color.Transparent.copy(0.1f),
                onClick = onSettingPost
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    )

}