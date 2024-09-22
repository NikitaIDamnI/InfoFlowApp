package com.example.common

import android.net.Uri
import com.google.gson.Gson

sealed class Screen(
    val route: String,
) {

    data object Home : Screen(ROUTE_HOME)
    data object Favorite : Screen(ROUTE_FAVORITE)
    data object World : Screen(ROUTE_WORLD)
    data object MainNews : Screen(ROUTE_MAIN_SCREEN_NEWS)
    data object Search : Screen(ROUTE_SEARCH){
        private const val ROUTE_FOR_ARGS = "search"
        fun getRouteWithArgs(category: String): String {
            return "$ROUTE_FOR_ARGS/${category}"
        }
    }
    data object DetailedNews : Screen(ROUTE_DETAILED_NEWS){
        private const val ROUTE_FOR_ARGS = "detailed_news"
        fun getRouteWithArgs(article: ArticleUI): String {
            val jsonFeedPost = Gson().toJson(article).encode()
            return "$ROUTE_FOR_ARGS/$jsonFeedPost"
        }

    }



    companion object {
        const val KEY_ARTICLE = "article"
        const val KEY_SEARCH = "search"

        const val ROUTE_HOME = "home"
        const val ROUTE_MAIN_SCREEN_NEWS = "main_screen_news"
        const val ROUTE_FAVORITE = "favorite"
        const val ROUTE_WORLD = "world"
        const val ROUTE_SEARCH = "search/{$KEY_SEARCH}"
        const val ROUTE_DETAILED_NEWS = "detailed_news/{$KEY_ARTICLE}"


    }
}

fun String.encode():String{
    return Uri.encode(this)
}