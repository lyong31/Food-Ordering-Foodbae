package com.foodbae.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

fun readJSONFile(context: Context, fileName: String):String? {
    val jsonString: String
    val charset: Charset = Charsets.UTF_8


    try {
        val `is`: InputStream = context.assets.open(fileName)
        val size: Int = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        jsonString = String(buffer, charset)

         //jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.message.toString()
        return null
    }
    return jsonString
}