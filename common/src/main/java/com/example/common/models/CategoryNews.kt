package com.example.common.models

import com.example.common.models.CategoryNews.entries
import kotlinx.serialization.Serializable

@Serializable
enum class CategoryNews(
    private val displayName: String,
) {

    ALL(displayName = "All"),
    SPORTS(displayName = "Sports"),
    HEALTH(displayName = "Health"),
    GENERAL(displayName = "General"),
    SCIENCE(displayName = "Science"),
    BUSINESS(displayName = "Business"),
    TECHNOLOGY(displayName = "Technology"),
    TOP_HEADLINES(displayName = "Top headlines"),
    ENTERTAINMENT(displayName = "Entertainment"),
    RECOMMENDATION(displayName = "Recommendation");

    override fun toString(): String {
        return displayName
    }

    companion object {
        fun toListCategory(activeCategory: CategoryNews): List<CategoryNews> {
            val baseList = entries.toTypedArray().toList()
            val filteredList = baseList.filter { it != RECOMMENDATION && it != TOP_HEADLINES }
            return when (activeCategory) {
                RECOMMENDATION, TOP_HEADLINES -> listOf(activeCategory) + filteredList
                else -> filteredList
            }
        }
    }
}
