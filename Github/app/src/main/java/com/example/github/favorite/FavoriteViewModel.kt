package com.example.github.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github.data.model.local.FavoriteRepo

class FavoriteViewModel(private val favoriteRepo: FavoriteRepo): ViewModel() {

    fun getFavorite() = favoriteRepo.favoriteDao.getLoadAllFavorite()

    class Factory(private val db: FavoriteRepo) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoriteViewModel(db) as T
    }
}