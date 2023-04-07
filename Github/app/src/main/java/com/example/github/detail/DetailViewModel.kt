package com.example.github.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.github.data.model.GithubResponse
import com.example.github.data.model.local.FavoriteRepo
import com.example.github.data.model.remote.ApiConfig
import com.example.github.utils.AllResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailViewModel(private val db: FavoriteRepo): ViewModel() {
    val resultDetailUser = MutableLiveData<AllResult>()
    val resultFollowersUser = MutableLiveData<AllResult>()
    val resultFollowingUser = MutableLiveData<AllResult>()
    val resultSuccessFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()

    private var _isFavorite = false

    fun setFavoriteUser(item: GithubResponse.ItemsItem?) {
        viewModelScope.launch {
            item?.let {
                if (_isFavorite) {
                    db.favoriteDao.delete(it)
                    resultDeleteFavorite.value = true
                } else {
                    db.favoriteDao.insertFavorite(it)
                    resultSuccessFavorite.value = true
                }
            }
            _isFavorite = !_isFavorite
        }
    }

    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = db.favoriteDao.getFavoriteById(id)
            if (user != null) {
                listenFavorite()
                _isFavorite = true
            }
        }
    }


    fun getDetailUsers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .apiService
                    .getUserDetail(username)
                emit(response)
            }.onStart {
                resultDetailUser.value = AllResult.Loading(true)
            }.onCompletion {
                resultDetailUser.value = AllResult.Loading(false)
            }.catch {
                it.printStackTrace()
                resultDetailUser.value = AllResult.Error(it)
            }.collect {
                resultDetailUser.value = AllResult.Success(it)
            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .apiService
                    .getFollowers(username)
                emit(response)
            }.onStart {
                resultFollowersUser.value = AllResult.Loading(true)
            }.onCompletion {
                resultFollowersUser.value = AllResult.Loading(false)
            }.catch {
                it.printStackTrace()
                resultFollowersUser.value = AllResult.Error(it)
            }.collect {
                resultFollowersUser.value = AllResult.Success(it)
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .apiService
                    .getFollowing(username)
                emit(response)
            }.onStart {
                resultFollowingUser.value = AllResult.Loading(true)
            }.onCompletion {
                resultFollowingUser.value = AllResult.Loading(false)
            }.catch {
                it.printStackTrace()
                resultFollowingUser.value = AllResult.Error(it)
            }.collect {
                resultFollowingUser.value = AllResult.Success(it)
            }
        }
    }

    class Factory(private val db: FavoriteRepo) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T
    }
}