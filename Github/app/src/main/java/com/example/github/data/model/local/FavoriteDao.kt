package com.example.github.data.model.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.github.data.model.GithubResponse

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(user: GithubResponse.ItemsItem)

    @Query("SELECT * FROM User")
    fun getLoadAllFavorite(): LiveData<MutableList<GithubResponse.ItemsItem>>

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun getFavoriteById(id: Int): GithubResponse.ItemsItem

    @Delete
    fun delete(user: GithubResponse.ItemsItem)
}