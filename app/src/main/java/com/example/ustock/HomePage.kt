package com.example.ustock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomePage() {
    // Create a scrollable content area
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Your scrollable content goes here
        // ...

        LineGraph(
            data = listOf(100f, 75f, 20f, 50f, 70f, 60f, 80f),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Adjust the height according to your needs
        )





        // Spacer to push the bottom bar to the bottom
        Spacer(modifier = Modifier.weight(1f))

        // Bottom bar with buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { /* Handle click for Button 1 */ }) {
                Text("Home")
            }
            Button(onClick = { /* Handle click for Button 2 */ }) {
                Text("Learn")
            }
            Button(onClick = { /* Handle click for Button 3 */ }) {
                Text("Create")
            }
            Button(onClick = { /* Handle click for Button 3 */ }) {
                Text("Compete")
            }
            Button(onClick = { /* Handle click for Button 3 */ }) {
                Text("Profile")
            }
        }
    }
}
