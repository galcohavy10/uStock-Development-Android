package com.example.ustock

import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.builtins.ListSerializer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

//This is a public constant to use in later APIs. All APIs use same server URL
object Constants {
    const val SERVER_URL = "https://enigmatic-plateau-21257.herokuapp.com"
}

class API {

    private val client = OkHttpClient() //reduced complexity to all use this client.
    private val json = Json { ignoreUnknownKeys = true }

    private fun createPostRequest(endpoint: String, body: String): Request {
        val url = "${Constants.SERVER_URL}$endpoint"
        val requestBody = body.toRequestBody("application/json".toMediaType())
        return Request.Builder().url(url).post(requestBody).build()
    }

    //Suspend keyword used to make this action non-blocking.
    //The main thread may be occupied with a user action and this is helpful async code.
    private suspend fun makeRequest(request: Request): String = withContext(Dispatchers.IO) {
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Server Error: ${response.code}")
        response.body?.string() ?: throw Exception("Invalid response")
    }

    //no use of private here is a good choice, many apis will call this on profiles.
    suspend fun getMyPosts(userID: String): List<Post> {
        val body = json.encodeToString(mapOf("userID" to userID))
        val request = createPostRequest("/api/getMyPosts", body)
        val postsJson = makeRequest(request)
        return json.decodeFromString(ListSerializer(Post.serializer()), postsJson)
    }

    //boilerplate, wouldn't work in practice but good job
    suspend fun sendPost(post: Post): Boolean {
        val body = json.encodeToString(post)
        val request = createPostRequest("/api/sendPost", body)
        return try {
            makeRequest(request)
            true
        } catch (e: Exception) {
            println("Error sending post: ${e.localizedMessage}")
            false
        }
    }
}
