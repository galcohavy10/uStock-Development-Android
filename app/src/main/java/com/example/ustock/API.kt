package com.example.ustock


import kotlinx.serialization.*
import java.net.HttpURLConnection
import java.net.URL
import java.io.*
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
import org.json.JSONObject
import okhttp3.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Base64

//This is a public constant to use in later APIs. All APIs use same server URL
object Constants {
    const val SERVER_URL = "https://enigmatic-plateau-21257.herokuapp.com"
}

class API {

    private val client = OkHttpClient() //reduced complexity to all use this client.
    private val json = Json { ignoreUnknownKeys = true }

    private suspend fun makeRequest(method: String, endpoint: String, body: String? = null): String = withContext(Dispatchers.IO) {
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

            inputStream.bufferedReader().readText()
        }
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
            makeRequest("POST", endpoint, body)
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
    // Required imports for OkHttp, JSONObject, Bitmap, and Base64

    fun fetchImageData(postID: String): Result<Bitmap> {
        val endpoint = "/api/fetchImageData"
        val formBody = FormBody.Builder()
            .add("postID", postID)
            .build()

        val request = Request.Builder()
            .url(endpoint)
            .post(formBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonResponse = JSONObject(response.body?.string())
            val base64Image = jsonResponse.optString("base64Image")

            val bytes = Base64.decode(base64Image, Base64.DEFAULT)
            val image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            completion(Result.success(image))
        }
    }

    fun fetchVideoURL(postID: String):Result<URL> {
        val endpoint = "/api/fetchVideoAuthorizedURL"
        val formBody = FormBody.Builder()
            .add("postID", postID)
            .build()

        val request = Request.Builder()
            .url(endpoint)
            .post(formBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonResponse = JSONObject(response.body?.string())
            val urlString = jsonResponse.optString("url")
            val url = URL(urlString)

            completion(Result.success(url))
        }
    }

    fun fetchAudioURL(postID: String):Result<URL>  {
        val endpoint = "/api/fetchAudioAuthorizedURL"
        val formBody = FormBody.Builder()
            .add("postID", postID)
            .build()

        val request = Request.Builder()
            .url(endpoint)
            .post(formBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonResponse = JSONObject(response.body?.string())
            val urlString = jsonResponse.optString("url")
            val url = URL(urlString)

            completion(Result.success(url))
        }
    }

}
//Hello this is a git test