package com.example.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.common.ArticleUI
import com.example.common.CategoryNews
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationState(
    val navHostController: NavHostController
) {

    private val _stateNavigationScreen = MutableStateFlow<Screen>(Screen.Home)




    fun navigationTo(route: Screen) {
        navHostController.navigate(route) {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true

        }
        _stateNavigationScreen.value = route
    }

    fun navigationToSearch(category: CategoryNews,content: List<ArticleUI>) {
        navHostController.navigate(Screen.Search(
            content = content,
            category = category
        ))
    }

    fun navigationToDetailedNews(article: ArticleUI) {
        navHostController.navigate(Screen.DetailedNews(article))
    }

}


@Composable
fun rememberNavigationState(
    navHostController: NavHostController? = null
): NavigationState {
    val controller = navHostController ?: rememberNavController()
    return remember { NavigationState(controller) }
}