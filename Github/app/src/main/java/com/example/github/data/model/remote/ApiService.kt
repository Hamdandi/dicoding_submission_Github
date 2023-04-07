package com.example.github.data.model.remote

import androidx.viewbinding.BuildConfig
import com.example.github.data.model.DetailUserResponse
import com.example.github.data.model.GithubResponse
import retrofit2.http.*

interface ApiService {

    @JvmSuppressWildcards
    @GET("search/users")
    @Headers("Authorization: token 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")
    suspend fun searchUser(
        @QueryMap params: Map<String, Any>
    ): GithubResponse

    @JvmSuppressWildcards
    @GET("users")
    @Headers("Authorization: token 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")
    suspend fun getUsers(
    ): MutableList<GithubResponse.ItemsItem>

    @JvmSuppressWildcards
    @GET("users/{username}")
    @Headers("Authorization: token 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")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): DetailUserResponse

    @JvmSuppressWildcards
    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    suspend fun getFollowers(
        @Path("username") username: String
    ): MutableList<GithubResponse.ItemsItem>

    @JvmSuppressWildcards
    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    suspend fun getFollowing(
        @Path("username") username: String
    ): MutableList<GithubResponse.ItemsItem>
}