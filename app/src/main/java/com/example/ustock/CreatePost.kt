package com.example.ustock
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import java.util.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import data_structures.Media
import data_structures.Post

//TODO: Incomplete
class CreatePost : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PostForm() // This is your Composable function
            }
        }
    }
}


@Composable
fun PostForm() {
    var id by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }
    var mediaType by remember { mutableStateOf("") }
    var mediaContent by remember { mutableStateOf("") }
    var mediaUrl by remember { mutableStateOf("") }
    var user by remember { mutableStateOf("") }

    Column {
        TextField(value = id, onValueChange = { id = it }, label = { Text("Id") })
        TextField(value = caption, onValueChange = { caption = it }, label = { Text("Caption") })
        TextField(value = mediaType, onValueChange = { mediaType = it }, label = { Text("Media Type") })
        TextField(value = mediaContent, onValueChange = { mediaContent = it }, label = { Text("Media Content") })
        TextField(value = mediaUrl, onValueChange = { mediaUrl = it }, label = { Text("Media Url") })
        TextField(value = user, onValueChange = { user = it }, label = { Text("User") })

        Button(onClick = { sendPostToServer(id, caption, mediaType, mediaContent, mediaUrl, user) }) {
            Text("Submit Post")
        }
    }
}

//TODO: Finish the send to post to server
fun sendPostToServer(id: String, caption: String, mediaType: String, mediaContent: String, mediaUrl: String, user: String) {
    val post = Post(
        id = id,
        caption = caption,
        media = Media(
            type = mediaType,
            content = mediaContent,
            url = mediaUrl
        ),
        user = user,
        createdAt = Date(), // Current time
        aspects = null,
        upvotes = null,
        downvotes = null,
        updatedAt = Date(),
        comments = null,
        tags = null,
        mentions = null

    )

    // Create API instance
    val api = API()

    // Coroutine scope for networking
    val coroutineScope = CoroutineScope(Dispatchers.IO)

    coroutineScope.launch {
        val result = api.sendPost(post) // Send the post to the server
        CoroutineScope(Dispatchers.IO).launch {

            if (result) {
                println("Post sent successfully")
            } else {
                println("Failed to send post")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostForm() {
    PostForm()
}
