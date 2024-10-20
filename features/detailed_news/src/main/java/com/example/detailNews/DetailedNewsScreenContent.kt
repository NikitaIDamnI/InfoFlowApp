package com.example.detailNews

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

const val ALPHA_TRANSPARENT = 0.1F

@Composable
fun DetailedNewsScreen(
    onBackPress: () -> Unit,
    onSettingPost: () -> Unit,
) {
    NewsScreen(
        viewModel = hiltViewModel(),
        onBackPress = onBackPress,
        onSettingPost = onSettingPost
    )
}

@Composable
@Suppress("LongMethod")
internal fun NewsScreen(
    viewModel: DetailedNewsScreenViewModel,
    onBackPress: () -> Unit,
    onSettingPost: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    val composition = rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("loadDetailedAnimation.json")
    )

    Scaffold(
        Modifier
            .fillMaxSize(),
        {
            TopBar(
                state = state,
                onBackPress = onBackPress,
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
                when (val stateLoaded = state.value.httpContent) {
                    is DetailedNewsScreenState.StateHttpContent.Error -> {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(100.dp),
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = ""
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(top = 150.dp),
                            text = stateLoaded.message
                        )
                    }

                    DetailedNewsScreenState.StateHttpContent.Loading -> {
                        LottieAnimation(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(150.dp),
                            composition = composition.value,
                            iterations = LottieConstants.IterateForever

                        )
                    }

                    is DetailedNewsScreenState.StateHttpContent.Success -> {
                        WebViewCompose(stateLoaded.htmlContent)
                    }

                    else -> {}
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    state: State<DetailedNewsScreenState>,
    onBackPress: () -> Unit,
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
            com.example.uikit.IconTopBar(
                icon = Icons.Default.ArrowBackIosNew,
                colorIcon = Color.White,
                colorBack = Color.Transparent.copy(ALPHA_TRANSPARENT),
                onClick = onBackPress

            )
        },
        actions = {
            IconTopBarFavorite(
                isFavorite = state.value.isFavorite,
                onClick = onAddFavorite,
                colorIcon = Color.White,
                colorBack = Color.Transparent.copy(ALPHA_TRANSPARENT),
            )
            Spacer(modifier = Modifier.width(10.dp))
            com.example.uikit.IconTopBar(
                icon = Icons.Default.MoreHoriz,
                colorIcon = Color.White,
                colorBack = Color.Transparent.copy(ALPHA_TRANSPARENT),
                onClick = onSettingPost
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    )
}

@Composable
fun IconTopBarFavorite(
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    colorIcon: Color = Color.Black,
    colorBack: Color = com.example.uikit.ColorNotActive,
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
