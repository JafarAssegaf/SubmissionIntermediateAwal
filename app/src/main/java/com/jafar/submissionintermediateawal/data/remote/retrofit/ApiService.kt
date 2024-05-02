package com.jafar.submissionintermediateawal.data.remote.retrofit

import com.jafar.submissionintermediateawal.data.remote.response.DetailStoryResponse
import com.jafar.submissionintermediateawal.data.remote.response.LoginResponse
import com.jafar.submissionintermediateawal.data.remote.response.PostStoryResponse
import com.jafar.submissionintermediateawal.data.remote.response.RegisterResponse
import com.jafar.submissionintermediateawal.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    // Register User
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ) : RegisterResponse

    // Login User
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    // Get all List User
    @GET("stories")
    suspend fun getAllUser(): StoryResponse

    // Get Detail Story
    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ) : DetailStoryResponse

    // Add new story
    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ) : PostStoryResponse
}