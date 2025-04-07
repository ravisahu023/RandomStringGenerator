package com.example.randomstringgenerator.data.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.randomstringgenerator.domain.model.RandomStringItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject

class RandomStringRepository(private val context: Context) {

    suspend fun safeQueryWithRetry(length: Int, maxRetries: Int = 2): RandomStringItem? {
        repeat(maxRetries) { attempt ->
            val result = fetchRandomString(length)
            if (result != null) return result
            delay(150) // Wait before retrying
        }
        return null // All retries failed
    }


    suspend fun fetchRandomString(limit: Int = 1): RandomStringItem? {
        delay(200)
        return withContext(Dispatchers.IO) {
            val uri = Uri.parse("content://com.iav.contestdataprovider/text")
            val bundle = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
            }
            try {
                val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.contentResolver.query(uri, null, bundle, null)
                } else {
                    context.contentResolver.query(
                        uri, null, "length = ?", arrayOf(limit.toString()), null
                    )
                }
                cursor?.use {
                    if (it.moveToFirst()) {
                        val json = it.getString(it.getColumnIndexOrThrow("data"))
                        val jsonObject = JSONObject(json)
                        val item = jsonObject.getJSONObject("randomText")
                        return@withContext RandomStringItem(
                            value = item.getString("value"),
                            length = item.getInt("length"),
                            created = item.getString("created")
                        )
                    } else {
                        throw IllegalStateException("No data received from provider.")
                    }
                }

            } catch (e: Exception) {
                Log.e("Repository", "ContentProvider query failed", e)
                throw e // Re-throw so ViewModel can handle
            }
        }
    }
}