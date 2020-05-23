package com.example.intheclouds.api

import androidx.lifecycle.LiveData
import com.example.intheclouds.model.CumulusResponse
import com.example.intheclouds.util.Constants
import com.example.intheclouds.util.GenericApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //@GET("?key=${Constants.PIXABAY_API_KEY}&q=cumulus&image_type=photo")
    @GET("api")
    fun getCumulusPhotos(
        @Query("key") key: String = Constants.PIXABAY_API_KEY,
        @Query("q") q: String = "cumulus",
        @Query("image_type") imageType: String = "photo",
        @Query("per_page") perPage: Int = 200
    ): LiveData<GenericApiResponse<CumulusResponse.Response>>
            //Call<CumulusResponse.Response>

}