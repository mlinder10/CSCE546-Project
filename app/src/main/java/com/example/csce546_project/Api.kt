package com.example.csce546_project.network

import com.example.csce546_project.models.*
import com.google.gson.annotations.SerializedName
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

val IS_DEBUG = false

// Store authentication token
object TokenManager {
    var token: String? = null
}

data class AuthResponse(@SerializedName("token") val token: String)

// Authentication interceptor to add token to requests
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain) = chain.proceed(
        chain.request().newBuilder().apply {
            TokenManager.token?.let { token ->
                addHeader("Authorization", "Bearer $token")
            }
        }.build()
    )
}

interface ApiService {
    @POST("auth/student/login")
    suspend fun login(
        @Body credentials: Map<String, String>
    ): AuthResponse

    @POST("auth/student/register")
    suspend fun register(
        @Body userData: Map<String, String>
    ): AuthResponse

    @GET("student/single")
    suspend fun fetchUserInfo(): Student

    @PATCH("student")
    suspend fun updateUserInfo(
        @Body userInfo: Map<String, String?>
    ): String

    @GET("section/student")
    suspend fun fetchSections(): List<Section>

    @GET("course/single/{sectionId}")
    suspend fun fetchCourseBySection(
        @Path("sectionId") sectionId: String
    ): Course

    @GET("topic/single/{topicId}")
    suspend fun fetchTopic(
        @Path("topicId") topicId: String
    ): Topic

    @POST("student/enroll")
    suspend fun joinSection(
        @Body enrollmentCode: Map<String, String>
    )

    @GET("question/{topicId}/{type}")
    suspend fun fetchQuestions(
        @Path("topicId") topicId: String,
        @Path("type") type: String
    ): List<Question>

    @GET("leaderboard/{sectionId}")
    suspend fun fetchLeaderboard(
        @Path("sectionId") sectionId: String
    ): Any

    @GET("leaderboard")
    suspend fun fetchLeaderboard(): Any

    @POST("leaderboard/update")
    suspend fun updateLeaderboard()
}

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(
            if (IS_DEBUG) "http://127.0.0.1:3000/api/"
            else "https://capstone-server-blush.vercel.app/api/"
        )
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService by lazy { retrofit.create(ApiService::class.java) }
}