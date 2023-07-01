package com.example.ustock

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
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.Image



@Composable
fun PostView(posts: List<Post>) {
    LazyColumn {
        items(posts) { post ->
            PostItem(post = post)
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID: ${post.id}")
            Text(text = "Caption: ${post.caption}")
            val context = LocalContext.current

            when (post.media.type) {
                "image" -> {
                    val painter = rememberImagePainter(
                        data = post.media.url,
                        builder = {
                            crossfade(true)
                        }
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Post image",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                "video" -> {
                    AndroidView(
                        factory = {
                            PlayerView(it).apply {
                                useController = false
                                player = SimpleExoPlayer.Builder(it).build().also { player ->
                                    val mediaItem = MediaItem.fromUri(post.media.url ?: "No URL")
                                    player.setMediaItem(mediaItem)
                                    player.prepare()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f)
                    )
                }
                "text" -> Text(text = post.media.content ?: "No content")
                else -> Text("Unsupported media type: ${post.media.type}")
            }
            Text(text = "User: ${post.user}")
            Text(text = "Aspects: ${post.aspects?.joinToString() ?: "No aspects"}")
            Text(text = "Upvotes: ${post.upvotes?.joinToString() ?: "No upvotes"}")
            Text(text = "Downvotes: ${post.downvotes?.joinToString() ?: "No downvotes"}")
            Text(text = "Created at: ${post.createdAt}")
            Text(text = "Updated at: ${post.updatedAt ?: "Not updated"}")
            Text(text = "Comments: ${post.comments?.joinToString() ?: "No comments"}")
            Text(text = "Tags: ${post.tags?.joinToString() ?: "No tags"}")
            Text(text = "Mentions: ${post.mentions?.joinToString() ?: "No mentions"}")
        }
    }
}


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
            downvotes = listOf("1", "2"),
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
