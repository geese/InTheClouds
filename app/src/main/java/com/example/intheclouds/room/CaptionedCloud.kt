package com.example.intheclouds.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "captioned_cloud")
data class CaptionedCloud(

    @ColumnInfo(name="id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @ColumnInfo(name="url")
    var url: String,

    @ColumnInfo(name="image_byte_array", typeAffinity = ColumnInfo.BLOB)
    var byteArray: ByteArray,

    @ColumnInfo(name="caption")
    var caption: String
)