package com.example.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
sealed class Screen {

    @Serializable
    data object Home : Screen()

    @Serializable
    data object Favorite : Screen()

    @Serializable
    data object World : Screen()

    @Serializable
    data object Main : Screen()

    @Serializable
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
