package com.example.ustock

import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.net.HttpURLConnection
import java.net.URL
import java.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class API {
    private val client = OkHttpClient()
    private val server = "https://enigmatic-plateau-21257.herokuapp.com"
    private val json = Json { ignoreUnknownKeys = true }

    private fun makeRequest(method: String, endpoint: String, body: String? = null): String {
        val url = URL("$server$endpoint")
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = method
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")

            if (body != null) {
                doOutput = true
                val outputWriter = OutputStreamWriter(outputStream)
                outputWriter.write(body)
                outputWriter.flush()
            }

            return inputStream.bufferedReader().readText()
        }
    }

    private suspend fun makeGetRequest(endpoint: String) = withContext(Dispatchers.IO) {
        makeRequest("GET", endpoint)
    }

    private suspend fun makePostRequest(endpoint: String, body: String) = withContext(Dispatchers.IO) {
        makeRequest("POST", endpoint, body)
    }


    suspend fun getMyPosts(userID: String): List<Post> {
        val endpoint = "/api/getMyPosts"
        val body = mapOf("userID" to userID)

        val requestBody = JSONObject(body).toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(server + endpoint)
            .post(requestBody)
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        if (!response.isSuccessful) throw Exception("Server Error: ${response.code}")

        val postsJson = response.body?.string() ?: throw Exception("Invalid response")

        return json.decodeFromString(postsJson)
    }


    suspend fun sendPost(post: Post): Boolean {
        //todo: change the endpoints
        val endpoint = "/api/sendPost"
        val body = Json.encodeToString(post) // Convert the Post object to a JSON string

        return try {
            makePostRequest(endpoint, body)
            true
        } catch (e: Exception) {
            println("Error sending post: ${e.localizedMessage}")
            false
        }
    }


}