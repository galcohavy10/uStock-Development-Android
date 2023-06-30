package com.example.ustock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Date


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
            Text(text = "Media Type: ${post.media.type}")
            Text(text = "Media Content: ${post.media.content ?: "No content"}")
            Text(text = "Media URL: ${post.media.url ?: "No URL"}")
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
