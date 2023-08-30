package com.example.ustock
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import data_structures.Post
import data_structures.wallet_model.Stock
import data_structures.wallet_model.Wallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//Where the PostView is shows with a wrapper and possibly other functions
class JourneyViewViewModel: ComponentActivity() {
    //Generates display
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                //Marco's ID: "646d7100141bdacde51e66b"
                JourneyView("644c34dd4f8a7aa9fcceaff8") // This is your Composable function
            }
        }
    }

    var api = API()
    private var posts: MutableState<List<Post>> = mutableStateOf(emptyList())
    private var wallet: MutableState<Wallet?> = mutableStateOf(null)
    private var stock: MutableState<Stock?> = mutableStateOf(null)
    //get posts from API
    private fun fetchPosts(userID: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedPosts = api.getMyPosts(userID)
            withContext(Dispatchers.Main) {
                posts.value = fetchedPosts
            }
        }
    }


    //Get both the wallet and stocks
    private fun fetchWallet(userID: String){
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedWallet = api.getWallet(userID)
            val fetchedStocks = api.getStock(fetchedWallet.id)
            withContext(Dispatchers.Main) {
                wallet.value = fetchedWallet
                stock.value = fetchedStocks
            }
        }
    }

    //The actual way to view the post after fetching the post information
    @Composable
    //again, you may use marco's username here: "646d7100141bdacde51e66b6" to make this work
    //James: "644c34dd4f8a7aa9fcceaff8"
    fun JourneyView(userID: String) {
        val viewModel = remember { JourneyViewViewModel() }

        LaunchedEffect(key1 = userID) {
            viewModel.fetchPosts(userID) //Change this to hard coded when testing
            viewModel.fetchWallet(userID)
        }

        val posts by viewModel.posts
        val wallet by viewModel.wallet
        val stock by viewModel.stock
        //Graph
        Text(text = "Wallet balance:${wallet?.balance}")
        Text(text = "Wallet transactions:${wallet?.transactions}")
        Text(text = "Stock wallet:${stock?.wallet}")
        Text(text = "Stock History:${stock?.history}")
        Text(text = "Stock symbol:${stock?.symbol}")


        if (posts.isNotEmpty()) {
            PostView(posts = posts.reversed()) //shows posts in order
        } else {
            //In case posts have not been retrieved
            //TODO: Maybe make a loading screen
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


}


