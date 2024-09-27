package com.example.common

import java.util.Date

 data class ArticleUI(
    val id: String,
    val title: String ,
    val description: String ,
    val imageUrl: String ,
    val url: String ,
    val content:String,
    val author: String ,
    val publishedAt: Date?
)