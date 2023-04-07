package com.example.github.data.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github.data.model.GithubResponse

@Database(
    entities = [GithubResponse.ItemsItem::class],
    version = 1,
    exportSchema = false)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}