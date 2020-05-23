package com.example.intheclouds.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// "object" makes it a Singleton
object MitchRetrofitBuilder {

    const val BASE_URL: String = "https://pixabay.com/"

    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService: ApiService by lazy{
        RetrofitBuilder.retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }
}