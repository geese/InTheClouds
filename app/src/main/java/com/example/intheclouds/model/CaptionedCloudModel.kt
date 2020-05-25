package com.example.intheclouds.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CaptionedCloudModel(
    var id: Long? = null,
    var url: String? = null,
    var byteArray: ByteArray? = null,
    var caption: String? = null
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CaptionedCloudModel

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}