package com.example.common

import com.example.common.CategoryNews.RECOMMENDATION
import com.example.common.CategoryNews.TOP_HEADLINES
import com.example.common.CategoryNews.entries


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
    ENTERTAINMENT(displayName = "Entertainment"),
    RECOMMENDATION(displayName = "Recommendation"),
    TOP_HEADLINES(displayName = "Top Headlines");

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



