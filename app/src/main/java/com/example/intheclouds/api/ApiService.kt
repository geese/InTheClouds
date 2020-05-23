package com.example.intheclouds.api

import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.util.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api")
    fun getCumulusPhotos(
        @Query("key") key: String = Constants.PIXABAY_API_KEY,
        @Query("q") q: String = "cumulus",
        @Query("image_type") imageType: String = "photo",
        @Query("per_page") perPage: Int = 200
    ): Call<Cumulus.Response>
}