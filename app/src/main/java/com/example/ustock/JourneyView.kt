package com.example.ustock
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JourneyViewViewModel() {
    var api = API()
    var posts: MutableState<List<Post>> = mutableStateOf(emptyList())

    fun fetchPosts(userID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedPosts = api.getMyPosts(userID)
            withContext(Dispatchers.Main) {
                posts.value = fetchedPosts
            }
        }
    }


}

@Composable
fun JourneyView(userID: String) {
    val viewModel = remember { JourneyViewViewModel() }

    LaunchedEffect(key1 = userID) {
        viewModel.fetchPosts(userID)
    }

    val posts by viewModel.posts

    if (posts.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(posts.reversed()) { post ->
                PostView(posts = posts) // You need to build this Composable separately
            }
        }
    } else {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "No Posts Yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Get Started!!!!",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}
