package com.example.uikit

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.common.models.CategoryNews



private const val PREFS_NAME = "theme_prefs"
private const val THEME_KEY = "is_dark_theme"

val GradientCard = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3D4052), Color(0xFF282a36),
        Color(0xFF09090C),
    )
)

val ColorNotActive = Color.Transparent.copy(alpha = 0.04f)

val MainBlueColor = Color(0xFF0288D1)

@Composable
fun CategoryCard(
    modifier: Modifier,
    categoryNews: CategoryNews,
    activeCategory: CategoryNews
) {
    val isSelected = categoryNews == activeCategory
    val colorText = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
    val colorBackground = if (isSelected) MainBlueColor else MaterialTheme.colorScheme.onBackground.copy(0.1f)
    Card(
        modifier = modifier,
        colors = CardColors(
            containerColor = colorBackground, contentColor = colorText,
            disabledContentColor = colorBackground, disabledContainerColor = colorText
        ),
        shape = CircleShape
    ) {
        Text(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
                .align(Alignment.Start),
            text = categoryNews .toString(),
            fontSize = 15.sp,
            color = colorText
        )
    }
}

@Composable
fun Title(
    modifier: Modifier,
    mainTitle: String,
    commentForTitle: String,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Box(
        modifier = modifier
    ) {
        Column {
            Text(
                modifier = Modifier,
                text = mainTitle,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                modifier = Modifier,
                text = commentForTitle,
                fontSize = 12.sp,
                color = color
            )
        }
    }

}

@Composable
fun ContentListItem(
    articles: com.example.common.models.ArticleUI,
    imageLoader: ImageLoader,
    onClick: (com.example.common.models.ArticleUI) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
            .clickable { onClick(articles) }
    ) {

        Box(
            modifier =
            Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(10.dp))
        ) {
            ImageNews(
                modifier = Modifier
                    .size(150.dp),
                url = articles.imageUrl,
                imageLoader = imageLoader,
            )
        }
        ContentRecommendation(
            modifier = Modifier
                .size(150.dp)
                .weight(1f)
                .padding(10.dp),
            articles = articles
        )


    }

}

@Composable
fun ContentRecommendation(
    modifier: Modifier = Modifier,
    articles: com.example.common.models.ArticleUI,
) {
    Box(modifier = modifier) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
            text = articles.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 4,
        )
        Text(
            modifier = Modifier.align(Alignment.BottomStart),
            text = articles.author,
            fontSize = 12.sp,
            color = Color.Gray,
            maxLines = 1
        )
    }
}


@Composable
fun ImageNews(
    modifier: Modifier = Modifier,
    url: String,
    imageLoader: ImageLoader,
) {
    if (url != "") {
        AsyncImage(
            modifier = modifier,
            model = url,
            contentDescription = null,
            imageLoader = imageLoader,
            contentScale = ContentScale.Crop,
        )
    } else {
        Image(
            modifier = modifier,
            painter = painterResource(id = com.example.common.R.drawable.the_main_image),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )
    }

}

@Composable
fun IconTopBar(
    modifier: Modifier = Modifier,
    colorIcon: Color = MaterialTheme.colorScheme.onBackground ,
    colorBack: Color = MaterialTheme.colorScheme.onBackground.copy(0.1f),
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(45.dp)
            .clip(CircleShape)
            .background(colorBack)
    ) {
        IconButton(onClick = { onClick() }) {
            Icon(
                modifier = Modifier
                    .size(35.dp)
                    .padding(5.dp),
                imageVector = icon,
                contentDescription = "",
                tint = colorIcon
            )
        }
    }
}

@Composable
fun rememberThemeState (): MutableState<Boolean>{
    val context = LocalContext.current
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val isDarkTheme = prefs.getBoolean(THEME_KEY, isSystemInDarkTheme())
   return remember {
        mutableStateOf<Boolean>(isDarkTheme)
    }
}


fun saveThemeOnAppClose(context: Context,isDarkTheme: Boolean) {
    val context = context
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    prefs.edit().putBoolean(THEME_KEY, isDarkTheme).apply()
}

