package com.example.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage

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
    val colorText = if (isSelected) Color.White else Color.Black
    val colorBackground = if (isSelected) MainBlueColor else ColorNotActive
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
            text = categoryNews.toString(),
            fontSize = 15.sp,
            color = colorText
        )
    }
}

@Composable
fun ContentListItem(
    articles: ArticleUI,
    imageLoader: ImageLoader,
    onClick: (ArticleUI) -> Unit
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
    articles: ArticleUI,
) {
    Box(modifier = modifier) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
            text = articles.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
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
            painter = painterResource(id = R.drawable.the_main_image),
            contentDescription = "",
            contentScale = ContentScale.Crop,
        )
    }

}

@Composable
fun IconTopBar(
    modifier: Modifier = Modifier,
    colorIcon: Color = Color.Black,
    colorBack:Color = ColorNotActive,
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