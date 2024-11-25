package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.common.models.ArticleUI
import com.example.common.models.CategoryNews
import kotlinx.coroutines.flow.MutableStateFlow

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

    fun navigationToSearch(category: CategoryNews, content: List<ArticleUI>) {
        navHostController.navigate(
            Screen.Search(
                content = content,
                category = category
            )
        )
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
