package com.example.intheclouds.api

import androidx.lifecycle.LiveData
import com.example.intheclouds.model.Pixabay
import com.example.intheclouds.util.Constants
import com.example.intheclouds.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Returning LiveData instead of Call<>
    // https://codingwithmitch.com/courses/model-view-intent-mvi-architecture/livedata-call-adapter-retrofit/


    @GET("api")
    fun getCumulusPhotos(
        @Query("key") key: String = Constants.PIXABAY_API_KEY,
        @Query("q") q: String = "fluffy+cloud",
        @Query("image_type") imageType: String = "photo",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 200
    ): LiveData<GenericApiResponse<Pixabay.Response>>
}