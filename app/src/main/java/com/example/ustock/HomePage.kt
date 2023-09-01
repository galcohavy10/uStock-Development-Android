package com.example.ustock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import data_structures.Post
import data_structures.wallet_model.Stock
import data_structures.wallet_model.Wallet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomePageViewModel: ComponentActivity() {
    //Generates display
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                //Marco's ID: "646d7100141bdacde51e66b"
                HomePage("644c34dd4f8a7aa9fcceaff8") // This is your Composable function
            }
        }
    }

    @Composable
    fun BottomBarWithButtons() {
        BottomAppBar(
            contentPadding = PaddingValues(),
            elevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* Handle Button 1 Click */ }) {
                    Text(text = "Home")
                }
                Button(onClick = { /* Handle Button 2 Click */ }) {
                    Text(text = "Learn")
                }
                Button(onClick = { /* Handle Button 2 Click */ }) {
                    Text(text = "Create")
                }
                Button(onClick = { /* Handle Button 2 Click */ }) {
                    Text(text = "Compete")
                }
                Button(onClick = { /* Handle Button 2 Click */ }) {
                    Text(text = "Profile")
                }
                // Add more buttons as needed
            }
        }
    }



    var api = API()
    private var posts: MutableState<List<Post>> = mutableStateOf(emptyList())
    private var wallet: MutableState<Wallet?> = mutableStateOf(null)
    private var stock: MutableState<Stock?> = mutableStateOf(null)

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
    //@Preview(showBackground = true)
    @Composable
    fun HomePage(userID: String) {
        val viewModel = remember { HomePageViewModel() }
        LaunchedEffect(key1 = userID) {
            viewModel.fetchWallet(userID) //Hard coded when testing
        }

        val wallet by viewModel.wallet
        val stock by viewModel.stock

        Scaffold(
            bottomBar = { BottomBarWithButtons() }
        ) { contentPadding ->
            // Create a scrollable content area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState())
            ) {

                //Graph
                StockGraphWithControls(stock)

                // Your scrollable content goes here
                Text(text = "No Tasks Yet")

                //Graph values testing
//                Text(text = "Wallet balance:${wallet?.balance}")
//                Text(text = "Wallet transactions:${wallet?.transactions}")
//                Text(text = "Stock wallet:${stock?.wallet}")
//                Text(text = "Stock History:${stock?.history}")
//                Text(text = "Stock symbol:${stock?.symbol}")

                // FloatingActionButton
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(56.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FloatingActionButton(
                        onClick = { /* Handle click for FAB */ },
                        backgroundColor = Color.Green,
                        contentColor = Color.White,
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                }

                // Spacer to push the content up
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}