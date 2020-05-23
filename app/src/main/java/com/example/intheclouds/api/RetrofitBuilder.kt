package com.example.intheclouds.api

import com.example.intheclouds.model.Cumulus
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder { //singleton

    const val BASE_URL: String = "https://pixabay.com/"

    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService: ApiService by lazy{
        retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }

    fun getCumulusPhotos() : Call<Cumulus.Response> = apiService.getCumulusPhotos()
}