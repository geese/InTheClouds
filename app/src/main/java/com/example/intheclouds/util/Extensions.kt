package com.example.intheclouds.util

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

fun getBase64String(bitmap: Bitmap) : String {
    Thread().run {
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
}