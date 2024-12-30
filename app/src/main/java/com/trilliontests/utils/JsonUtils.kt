package com.trilliontests.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object JsonUtils {
    inline fun <reified T> parseJsonFromAssets(context: Context, fileName: String): T {
        try {
            val jsonString = context.assets.open(fileName)
                .bufferedReader()
                .use { it.readText() }
            return Gson().fromJson(jsonString, object : TypeToken<T>() {}.type)
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }
} 