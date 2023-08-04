package com.example.ustock


import java.net.URL
import java.io.*
import kotlinx.serialization.json.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.builtins.ListSerializer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Base64
import com.example.ustock.Constants.server
import data_structures.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.MalformedURLException

//This is a public constant to use in later APIs. All APIs use same server URL
//For James: When pushing to the github, first fetch then use git push origin master
object Constants {
    const val server = "https://enigmatic-plateau-21257.herokuapp.com"
}

class API {

    private val client = OkHttpClient() //reduced complexity to all use this client.
    private val json = Json { ignoreUnknownKeys = true }

    private fun createPostRequest(endpoint: String, body: String): Request {
        val url = "$server$endpoint"
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

    suspend fun getMyPosts(userID: String): List<Post> {
        val body = json.encodeToString(mapOf("userID" to userID))
        val request = createPostRequest("/api/getMyPosts", body)
        val postsJson = makeRequest(request)
        return json.decodeFromString(ListSerializer(Post.serializer()), postsJson)
    }

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

    suspend fun getUserID(username: String, password: String): Result<String> {
        val endpoint = "/api/getUserID"
        val body = mapOf("username" to username, "password" to password)
        val bodyString = JSONObject(body).toString()
        val requestBody = bodyString.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(server + endpoint)
            .post(requestBody)
            .build()

        //Error Handling
        return withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Result.failure(Throwable("Server Error: ${response.code}"))
                    } else {
                        val userID = response.body?.string() ?: throw Exception("Invalid response")
                        if (userID.isNotEmpty()) {
                            Result.success(userID)
                        } else {
                            Result.failure(Throwable("Invalid JSON"))
                        }
                    }
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    //Fetch Media from API
    //get any image, this is getting the whole file. the response will look like base64 data.
    suspend fun fetchImageData(postID: String, completion: (Result<Bitmap>) -> Unit) {
        val endpoint = "/api/fetchImageData"
        val parameters: Map<String, Any> = mapOf("postID" to postID)
        val jsonObject = JSONObject()
        for ((key, value) in parameters) {
            jsonObject.put(key, value)
        }
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(server + endpoint)
            .post(requestBody)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .build()

        //Error Handling
        CoroutineScope(Dispatchers.IO).launch {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        completion(Result.failure(IOException("Unexpected code $response")))
                    }
                } else {
                    val jsonResponse = JSONObject(response.body?.string())
                    val base64Image = jsonResponse.optString("base64Image")
                    val bytes = Base64.decode(base64Image, Base64.DEFAULT)
                    val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                    withContext(Dispatchers.Main) {
                        completion(Result.success(image))
                    }
                }
            }
        }
    }

    //Get Video API
    suspend fun fetchVideoURL(postID: String) : Result<URL> {
        val endpoint = "/api/fetchVideoAuthorizedURL"
        val parameters: Map<String, Any> = mapOf("postID" to postID)

        val jsonObject = JSONObject()
        for ((key, value) in parameters) {
            jsonObject.put(key, value)
        }
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(server + endpoint)
            .post(requestBody)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .build()

        //Error Handling
        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Result.failure(IOException("Unexpected code $response"))
                } else {
                    val jsonResponse = JSONObject(response.body?.string())
                    val urlString = jsonResponse.optString("url")
                    try {
                        val url = URL(urlString)
                        Result.success(url)
                    } catch (e: MalformedURLException) {
                        Result.failure(e)
                    }
                }
            }
        }
    }


    //Get Audio API
    //May be deprecated
    fun fetchAudioURL(postID: String, completion: (Result<URL>) -> Unit) {
        val endpoint = "/api/fetchAudioAuthorizedURL"
        val parameters: Map<String, Any> = mapOf("postID" to postID)

        val jsonObject = JSONObject()
        for ((key, value) in parameters) {
            jsonObject.put(key, value)
        }
        val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(server + endpoint)
            .post(requestBody)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        completion(Result.failure(IOException("Unexpected code $response")))
                    }
                } else {
                    val jsonResponse = JSONObject(response.body?.string())
                    val urlString = jsonResponse.optString("url")
                    try {
                        val url = URL(urlString)
                        withContext(Dispatchers.Main) {
                            completion(Result.success(url))
                        }
                    } catch (e: MalformedURLException) {
                        withContext(Dispatchers.Main) {
                            completion(Result.failure(e))
                        }
                    }
                }
            }
        }
    }


}