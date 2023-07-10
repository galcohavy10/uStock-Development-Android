package com.example.ustock

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Date
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

//Compile all the individual Postitems into a scrollable list
@Composable
fun PostView(posts: List<Post>) {
    LazyColumn {
        items(posts) { post ->
            PostItem(post = post)
        }
    }
}

//View one post
@Composable
fun PostItem(post: Post) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            val isUpvoted = remember { mutableStateOf(false) }
            val isDownvoted = remember { mutableStateOf(false) }

            Text(text = "ID: ${post.id}")
            Text(text = "Caption: ${post.caption}")
            val context = LocalContext.current

            val mediaUrl = post.media.url ?: null
            var postImage: Bitmap? = null
            var postVideoURL: String? = null
            var postAudioURL: String? = null
            var api = API()

            when (post.media.type) {
                "image" -> {
                    if (postImage == null) {
                        //imageLoading = true
                        CoroutineScope(Dispatchers.IO).launch {
                            val result = api.fetchImageData(post.id)
                            withContext(Dispatchers.Main) {
                                //imageLoading = false
                                if (result.isSuccess) {
                                    val image = result.getOrNull()
                                    postImage = image
                                } else if (result.isFailure) {
                                    println("Failed to fetch image data: ${result.exceptionOrNull()}")
                                }
                            }
                        }
                    }
                }

                "video" -> {
                    if (postVideoURL == null) {
                        //videoLoading = true
                        CoroutineScope(Dispatchers.IO).launch {
                            val result = api.fetchVideoURL(post.id)
                            withContext(Dispatchers.Main) {
                                //videoLoading = false
                                if (result.isSuccess) {
                                    val url = result.getOrNull()
                                    postVideoURL = url
                                } else if (result.isFailure) {
                                    println("Failed to fetch video URL: ${result.exceptionOrNull()}")
                                }

                            }
                        }
                    }
                }

                "audio" -> {
                    if (postAudioURL == null) {
                        //audioLoading = true
                        CoroutineScope(Dispatchers.IO).launch {
                            val result = api.fetchAudioURL(post.id)
                            withContext(Dispatchers.Main) {
                                //audioLoading = false
                                if (result.isSuccess) {
                                    val url = result.getOrNull()
                                    postAudioURL = url
                                } else if (result.isFailure) {
                                    println("Failed to fetch audio URL: ${result.exceptionOrNull()}")
                                }
                            }
                        }
                    }
                }

                "text" -> Text(text = post.media.content ?: "No content")
                else -> Text("Unsupported media type: ${post.media.type}")
            }

            //TODO Ask what user does
            Text(text = "User: ${post.user}")
            //TODO show the aspects as a list and probably need to decode it later
            Text(text = "Number of Aspects: ${post.aspects?.size ?: "No aspects"}")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    //TODO: Make the application remember the upvote with the user ID
                    //TODO: If one button is pressed, make sure the other button is off. (Both cannot be on)
                    Button(
                        onClick = { isUpvoted.value = !isUpvoted.value },
                        colors = ButtonDefaults.buttonColors(backgroundColor = if (isUpvoted.value) Color.Green else Color.White),
                        //  modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Filled.ArrowCircleUp, contentDescription = null)
                        Text(text = "${post.upvotes?.size ?: "0"}")
                    }

                    //TODO: Make the button red when pushed
                    Button(
                        onClick = { isDownvoted.value = !isDownvoted.value },
                        colors = ButtonDefaults.buttonColors(backgroundColor = if (isDownvoted.value) Color.Green else Color.White)
                    ) {
                        Icon(Icons.Filled.ArrowCircleDown, contentDescription = null)
                        Text(text = "${post.downvotes?.size ?: "0"}")
                    }
                }

                Button(
                    onClick = { /*TODO: handle click event*/ },
                    modifier = Modifier.size(50.dp)
                ) {
                    Text(text = "Comments: ${post.comments?.size ?: "0"}")
                }
            }
            Text(text = "Created at: ${post.createdAt}")


            Text(text = "Tags: ${post.tags?.joinToString() ?: "No tags"}")
            Text(text = "Mentions: ${post.mentions?.joinToString() ?: "No mentions"}")
        }
    }
}

//Easy debug function
//However does not work now since it is unable to parse Glide. TBH, idk if glide is working
@Preview(showBackground = true)
@Composable
fun PreviewPostView() {
    val media = Media(
        type = "image",
        content = "An image of a sunset over the ocean",
        url = "https://my-cloud-bucket.com/my_images/sunset.jpg"
    )

    val posts = listOf(
        Post(
            id = "1",
            caption = "Caption for the post",
            media = media,
            user = "User 1",
            aspects = listOf("1", "2"),
            upvotes = listOf("1", "2"),
            downvotes = listOf(),
            createdAt = Date(),
            updatedAt = Date(),
            comments = listOf("1", "2"),
            tags = listOf("1", "2"),
            mentions = listOf("1", "2")
        ),
        Post(
            id = "2",
            caption = "Caption for the post",
            media = media,
            user = "User 1",
            aspects = listOf("1", "2"),
            upvotes = listOf("1", "2"),
            downvotes = listOf("1", "2"),
            createdAt = Date(),
            updatedAt = Date(),
            comments = listOf("1", "2"),
            tags = listOf("1", "2"),
            mentions = listOf("1", "2")
        )

        // You can add more posts here for previewing.
    )
    PostView(posts)
}
