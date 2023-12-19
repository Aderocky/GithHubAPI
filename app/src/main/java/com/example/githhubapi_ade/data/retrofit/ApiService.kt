package com.example.githhubapi_ade.data.retrofit

import com.example.githhubapi_ade.data.response.DetailUserResponse
import com.example.githhubapi_ade.data.response.FollowResponseItem
import com.example.githhubapi_ade.data.response.GithubResponse
import com.example.githhubapi_ade.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getUsers(): Call<List<ItemsItem>>

    @GET("search/users")
    fun getSearchUser(
        @Query("q") username: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<FollowResponseItem>>
}


