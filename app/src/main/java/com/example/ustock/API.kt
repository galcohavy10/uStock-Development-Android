package com.example.ustock

import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.net.HttpURLConnection
import java.net.URL
import java.io.*



class API {
    val server = "https://enigmatic-plateau-21257.herokuapp.com"
    val json = Json { ignoreUnknownKeys = true }
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
        //TODO: Change the endpoints
        val endpoint = "/api/getMyPosts"
        val body = Json.encodeToString(mapOf("userID" to userID))

        val result = makePostRequest(endpoint, body)

        return json.decodeFromString<List<Post>>(result)
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