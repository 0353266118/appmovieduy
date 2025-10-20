package com.example.appmoive.data.api

import com.example.appmoive.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiClient {
    // Tạo đối tượng Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Tạo ra ApiService để có thể gọi từ bất cứ đâu
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}