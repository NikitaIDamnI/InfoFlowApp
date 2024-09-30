package com.example.database.dao

import android.os.FileObserver.DELETE
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.models.ArticleDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    suspend fun getAll(): List<ArticleDBO>

    @Query("SELECT COUNT(*) FROM articles WHERE  url = :url")
    suspend fun checkFavorite(url: String): Int

    @Query("SELECT * FROM articles")
    fun observeAll(): Flow<List<ArticleDBO>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(articles: ArticleDBO)

    @Query("DELETE  FROM articles WHERE url = :url")
    suspend fun remove(url: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: List<ArticleDBO>)

    @Delete
    suspend fun remove(articles: List<ArticleDBO>)

    @Query("DELETE FROM articles")
    suspend fun clean()
}