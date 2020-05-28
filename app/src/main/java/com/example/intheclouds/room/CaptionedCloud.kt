package com.example.intheclouds.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "captioned_cloud")
data class CaptionedCloud(

    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name="url")
    var url: String,

    @ColumnInfo(name="image_byte_array", typeAffinity = ColumnInfo.BLOB)
    var byteArray: ByteArray? = null,

    @ColumnInfo(name="caption")
    var caption: String

) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CaptionedCloud

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}