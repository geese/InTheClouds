package com.example.intheclouds.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.intheclouds.R
import java.io.ByteArrayOutputStream


fun Bitmap.toByteArray() : ByteArray {
    var baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    return baos.toByteArray()
}

fun getSampleByteArrays(context: Context): ArrayList<ByteArray> {

    return ArrayList<ByteArray>().apply {

        // add the first sample image to the list
        var inputStream = context.resources.openRawResource(R.raw.above_it_all)
        var bitmap = BitmapFactory.decodeStream(inputStream)
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        this.add(baos.toByteArray())

        // add the second sample image to the list
        inputStream = context.resources.openRawResource(R.raw.graple)
        bitmap = BitmapFactory.decodeStream(inputStream)
        baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        this.add(baos.toByteArray())
    }
}