package com.example.github

import androidx.lifecycle.*
import com.example.github.data.model.local.SettingPreferences
import com.example.github.data.model.remote.ApiConfig
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.example.github.utils.AllResult

class MainViewModel(private val preferences: SettingPreferences): ViewModel() {

    val resultUser = MutableLiveData<AllResult>()

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    fun getUsers() {
        viewModelScope.launch {
                flow {
                    val response = ApiConfig
                        .apiService
                        .getUsers()
                    emit(response)
                }.onStart {
                    resultUser.value = AllResult.Loading(true)
                }.onCompletion {
                    resultUser.value = AllResult.Loading(false)
                }.catch {
                    it.printStackTrace()
                    resultUser.value = AllResult.Error(it)
                }.collect {
                    resultUser.value = AllResult.Success(it)
                }
        }
    }

    fun getSearchUser(username : String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .apiService
                    .searchUser(mapOf(
                        "q" to username
                    ))
                emit(response)
            }.onStart {
                resultUser.value = AllResult.Loading(true)
            }.onCompletion {
                resultUser.value = AllResult.Loading(false)
            }.catch {
                it.printStackTrace()
                resultUser.value = AllResult.Error(it)
            }.collect {
                resultUser.value = AllResult.Success(it.items)
            }
        }
    }

    class Factory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(pref) as T
    }
}