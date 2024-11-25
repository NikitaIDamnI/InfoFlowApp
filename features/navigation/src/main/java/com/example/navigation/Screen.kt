package com.example.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
sealed class Screen {

    @Serializable
    @SerialName("home")
    data object Home : Screen()

    @Serializable
    @SerialName("favorite")
    data object Favorite : Screen()

    @Serializable
    @SerialName("world")
    data object World : Screen()

    @Serializable
    @SerialName("main")
    data object Main : Screen()

    @Serializable
    @SerialName("search")
    data class Search(
        val category: CategoryNews,
        val content: List<ArticleUI>
    ) : Screen() {
        companion object {
            val typeMap = mapOf(
                typeOf<CategoryNews>() to toNavType<CategoryNews>(),
                typeOf<List<ArticleUI>>() to toNavType<List<ArticleUI>>(true)
            )

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<Search>(typeMap)
        }
    }

    @Serializable
    @SerialName("detailedNews")
    data class DetailedNews(
        val articleUI: ArticleUI
    ) : Screen() {
        companion object {
            val typeMap = mapOf(typeOf<ArticleUI>() to toNavType<ArticleUI>())

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<DetailedNews>(typeMap)
        }
    }
}
