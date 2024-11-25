package com.example.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "articles",
    indices = [Index(value = ["url"], unique = true)]
)
data class ArticleDBO(
    @Embedded(prefix = "source") val source: Source?,
    @ColumnInfo("author") val author: String?,
    @ColumnInfo("title") val title: String?,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("url") val url: String?,
    @ColumnInfo("urlToImage") val urlToImage: String?,
    @ColumnInfo("publishedAt") val publishedAt: Date?,
    @ColumnInfo("content") val content: String?,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

data class Source(
    val id: String,
    val name: String
)
