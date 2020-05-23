package com.example.intheclouds.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

object CumulusResponse {

    data class Response(

        @Expose
        val total: Int? = 0,
        @Expose
        val totalHits: Int? = 0,

        @Expose
        @SerializedName("hits")
        val hits: List<CumulusHit>

    )
    data class CumulusHit(

        @Expose
        @SerializedName("id")
        val id: Int? = -1,

        @Expose
        @SerializedName("pageURL")
        val pageURL: String? = null,

        @Expose
        @SerializedName("type")
        val type: String? = null,

        @Expose
        @SerializedName("tags")
        val tags: String? = null,

        @Expose
        @SerializedName("previewURL")
        val previewURL: String? = null,

        @Expose
        @SerializedName("previewWidth")
        val previewWidth: Int? = null,

        @Expose
        @SerializedName("previewHeight")
        val previewHeight: Int? = null,

        @Expose
        @SerializedName("webformatURL")
        val url: String? = null,

        @Expose
        @SerializedName("webformatWidth")
        val webformatWidth: Int? = null,

        @Expose
        @SerializedName("webformatHeight")
        val webformatHeight: Int? = null,

        @Expose
        @SerializedName("largeImageURL")
        val largeImageURL: String? = null,

        @Expose
        @SerializedName("imageWidth")
        val imageWidth: Int? = null,

        @Expose
        @SerializedName("imageHeight")
        val imageHeight: Int? = null,

        @Expose
        @SerializedName("imageSize")
        val imageSize: Long = 0,

        @Expose
        @SerializedName("views")
        val views: Long = 0,

        @Expose
        @SerializedName("downloads")
        val downloads: Long = 0,

        @Expose
        @SerializedName("favorites")
        val favorites: Long = 0,

        @Expose
        @SerializedName("likes")
        val likes: Long = 0,

        @Expose
        @SerializedName("comments")
        val comments: Int? = null,

        @Expose
        @SerializedName("user_id")
        val user_id: Int? = null,

        @Expose
        @SerializedName("user")
        val user: String? = null,

        @Expose
        @SerializedName("userImageURL")
        val userImageURL: String? = null
    )
}


