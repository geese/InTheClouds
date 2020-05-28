package com.example.intheclouds.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

object Pixabay {

    data class Response(

        @Expose
        val totalHits: Int? = 0,

        @Expose
        @SerializedName("hits")
        val cloudImages: List<CloudImage>

    )
    data class CloudImage(

        @Expose
        @SerializedName("webformatURL")
        val url: String? = null,

        @Expose
        @SerializedName("id")
        val id: Long? = null
    )
}


