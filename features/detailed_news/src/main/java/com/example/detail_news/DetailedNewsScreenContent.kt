package com.example.detail_news

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavigatorState
import coil.ImageLoader
import coil.request.CachePolicy
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.common.ArticleUI
import com.example.common.ColorNotActive
import com.example.common.IconTopBar
import com.example.common.ImageNews
import com.example.common.getDatePublication
import com.example.navigation.NavigationState

@Composable
fun DetailedNewsScreen(
    articleUIID: ArticleUI,
    navState: NavigationState,
    onBackPressed: () -> Unit,
    onSettingPost: () -> Unit
) {
    NewsScreen(
        viewModel = hiltViewModel(),
        onBackPressed = onBackPressed,
        onSettingPost = onSettingPost
    )

}

@Composable
internal fun NewsScreen(
    viewModel: DetailedNewsScreenViewModel,
    onBackPressed: () -> Unit,
    onSettingPost: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    val composition = rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("loadDetailedAnimation.json")
    )
    Log.d("Recomposition", "NewsScreen")

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(
                state = state,
                onBackPressed = onBackPressed,
                onAddFavorite = { viewModel.addToFavorites() },
                onSettingPost = onSettingPost
            )
        },
        containerColor = Color.White,

    ) { paddingValues ->
        paddingValues
        Column {

            Box(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                val htmlContent = state.value.htmlContent

                if (htmlContent != null) {
                    WebViewCompose(htmlContent)
                } else {
                    LottieAnimation(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(150.dp),
                        composition = composition.value,
                        iterations = LottieConstants.IterateForever

                    )
                }


            }
        }

    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.updateFavoritesCache()
        }
    }

}

@Composable
private fun Title(imageLoader: ImageLoader, articleUI: ArticleUI) {
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
                    text = articleUI.publishedAt.getDatePublication(),
                    fontSize = 15.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(60.dp))

            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    state: State<DetailedNewsScreenState>,
    onBackPressed: () -> Unit,
    onAddFavorite: () -> Unit,
    onSettingPost: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.White,
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
            IconTopBarFavorite(
                isFavorite = state.value.isFavorite,
                onClick = onAddFavorite,
                colorIcon = Color.White,
                colorBack = Color.Transparent.copy(0.1f),
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

@Composable
fun IconTopBarFavorite(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    colorIcon: Color = Color.Black,
    colorBack: Color = ColorNotActive,
    onClick: () -> Unit
) {
    Log.d("Content_Log", "isFavorite: $isFavorite")

    Box(
        modifier = modifier
            .size(45.dp)
            .clip(CircleShape)
            .background(colorBack)
    ) {
        IconButton(onClick = { onClick() }) {
            if (isFavorite) {
                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .padding(5.dp),
                    imageVector = Icons.Rounded.Bookmark,
                    contentDescription = "",
                    tint = colorIcon
                )
            } else {
                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .padding(5.dp),
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = "",
                    tint = colorIcon
                )
            }

        }
    }
}