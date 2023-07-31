package com.example.ustock

import android.graphics.Bitmap
import android.media.AudioManager
import android.media.MediaPlayer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import java.net.URL
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import kotlinx.coroutines.withContext
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import data_structures.Media
import data_structures.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//Compile all the individual Postitems into a scrollable list
@Composable
fun PostView(posts: List<Post>) {
    LazyColumn(
        modifier = Modifier.background(Color.LightGray) // Adds a light gray background
    ) {
        items(posts) { post ->
            PostItem(
                post = post,
                modifier = Modifier.padding(vertical = 8.dp) // Adds vertical padding to create space between items
            )
        }
    }
}


//View one post
@Composable
fun PostItem(post: Post, modifier: Modifier = Modifier) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            val isUpvoted = remember { mutableStateOf(false) }
            val isDownvoted = remember { mutableStateOf(false) }

            Text(text = "ID: ${post.id}")
            Text(text = "Caption: ${post.caption}")
            val context = LocalContext.current
            var postAudioURL: URL? = null
            var api = API()
            Text(text = "Media Type: ${post.media.type ?: "No tags"}")
            Text(text = "Media url: ${post.media.url ?: "No tags"}")
            when (post.media.type) {
                "image" -> {
                    val postImage = remember { mutableStateOf<Bitmap?>(null) } // Declare postImage as State<Bitmap?>

                    LaunchedEffect(key1 = post.id) {
                        api.fetchImageData(post.id) { result ->
                            if (result.isSuccess) {
                                val image = result.getOrNull()
                                postImage.value = image // update the mutable state with the fetched image
                            } else if (result.isFailure) {
                                println("Failed to fetch image data: ${result.exceptionOrNull()}")
                            }
                        }
                    }
                    postImage.value?.let {
                        val imageBitmap = remember { it.asImageBitmap() } // Convert Bitmap to ImageBitmap
                        Image(bitmap = imageBitmap, contentDescription = "Post image")
                    }
                }

            /*"video" -> {
                    val context = LocalContext.current

                    val exoPlayer = remember {
                        ExoPlayer.Builder(context).build().apply {
                            val mediaItem = MediaItem.fromUri(url) // put here your hardcoded URL
                            setMediaItem(mediaItem)
                            prepare()
                            playWhenReady = true
                        }
                    }

                    AndroidView({ StyledPlayerView(it).apply { player = exoPlayer } })

                    DisposableEffect(Unit) {
                        onDispose {
                            exoPlayer.release()
                        }
                    }
                }*/

                "video" -> {
                    val context = LocalContext.current
                    val postVideoURL = remember { mutableStateOf<URL?>(null) }
                    val exoPlayer = remember {
                        ExoPlayer.Builder(context).build()
                    }

                    LaunchedEffect(key1 = post.id) {
                        CoroutineScope(Dispatchers.IO).launch {
                            api.fetchVideoURL(post.id).let { result ->
                                withContext(Dispatchers.Main) {
                                    if (result.isSuccess) {
                                        postVideoURL.value = result.getOrNull()

                                        // Prepare the ExoPlayer with the source
                                        val mediaItem = MediaItem.fromUri(postVideoURL.value.toString())
                                        exoPlayer.setMediaItem(mediaItem)
                                        exoPlayer.prepare()

                                        // Play the video
                                        exoPlayer.playWhenReady = true
                                    } else if (result.isFailure) {
                                        println("Failed to fetch video URL: ${result.exceptionOrNull()}")
                                    }
                                }
                            }
                        }
                    }

                    AndroidView(
                        factory = { StyledPlayerView(it).apply { player = exoPlayer } },
                        update = { view -> view.player = exoPlayer }
                    )

                    DisposableEffect(Unit) {
                        onDispose {
                            exoPlayer.release()
                        }
                    }
                }

                "audio" -> {
                    val ctx = LocalContext.current
                    val mediaPlayer = MediaPlayer()
                    Text(
                        modifier = Modifier.padding(6.dp),
                        text = "Play Audio from URL",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(7.dp),
                        onClick = {
                            var audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                            try {
                                mediaPlayer.setDataSource(audioUrl)
                                mediaPlayer.prepare()
                                mediaPlayer.start()

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            Toast.makeText(ctx, "Audio started playing..", Toast.LENGTH_SHORT).show()

                        }) {
                        Text(text = "Play Audio")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        modifier = Modifier
                            .width(300.dp)
                            .padding(7.dp),
                        onClick = {
                            if (mediaPlayer.isPlaying) {
                                mediaPlayer.stop()
                                mediaPlayer.reset()
                                mediaPlayer.release()
                                Toast.makeText(ctx, "Audio has been  paused..", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(ctx, "Audio not played..", Toast.LENGTH_SHORT).show()
                            }


                        }) {
                        // on below line we are specifying text for button.
                        Text(text = "Pause Audio")
                    }

//                    if (postAudioURL == null) {
//                        LaunchedEffect(key1 = post.id) {
//                            api.fetchAudioURL(post.id) { result ->
//                                if (result.isSuccess) {
//                                    val url = result.getOrNull()
//                                    postAudioURL = url
//                                } else if (result.isFailure) {
//                                    println("Failed to fetch audio URL: ${result.exceptionOrNull()}")
//                                }
//                            }
//                        }
//                    }
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

//                var showComments by remember { mutableStateOf(false) }
//
//                if (showComments) {
//                    CommentsScreen(post.comments ?: emptyList())
//                } else {
//                    // ...Your post code here...
//
//                    Button(
//                        onClick = { showComments = true }
//                    ) {
//                        Text(text = "Comments: ${post.comments?.size ?: "0"}")
//                    }
//                }
                Button(
                        onClick = {  }
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

//@Composable
//fun CommentsScreen(comments: List<Comment>) {
//    LazyColumn {
//        items(comments) { comment ->
//            Text(text = comment.text)  // Assuming Comment has a 'text' property
//        }
//    }
//}

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
