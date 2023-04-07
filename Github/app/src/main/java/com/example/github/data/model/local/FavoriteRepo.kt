package com.example.github.data.model.local

import android.content.Context
import androidx.room.Room

class FavoriteRepo(private val context: Context) {
    private val db = Room.databaseBuilder(context, FavoriteDatabase::class.java, "usergithub.db")
        .allowMainThreadQueries()
        .build()

    val favoriteDao = db.favoriteDao()
}