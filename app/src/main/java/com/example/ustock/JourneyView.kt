package com.example.ustock
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
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

//todo: Only have the get post working

class JourneyViewViewModel: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                JourneyView("646d7100141bdacde51e66b6") // This is your Composable function
            }
        }
    }

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

    @Composable
    //again, you may use marco's username here: "646d7100141bdacde51e66b6" to make this work
    fun JourneyView(userID: String) {
        val viewModel = remember { JourneyViewViewModel() }

        LaunchedEffect(key1 = userID) {
            viewModel.fetchPosts(userID)
        }

        val posts by viewModel.posts

        if (posts.isNotEmpty()) {
            PostView(posts = posts.reversed()) // directly use PostView here
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

//    fun JourneyView(userID: String) {
//        val viewModel = remember { JourneyViewViewModel() }
//
//        LaunchedEffect(key1 = userID) {
//            viewModel.fetchPosts(userID)
//        }
//
//        val posts by viewModel.posts
//
//        if (posts.isNotEmpty()) {
//            LazyColumn(
//                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
//            ) {
//                items(posts.reversed()) { post ->
//                    PostView(post = post) // You need to build this Composable separately
//                }
//            }
//        } else {
//            Column(modifier = Modifier.padding(8.dp)) {
//                Text(
//                    text = "No Posts Yet",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Text(
//                    text = "Get Started!!!!",
//                    fontSize = 16.sp,
//                    color = Color.Gray
//                )
//            }
//        }
//    }

}


