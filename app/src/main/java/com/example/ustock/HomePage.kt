package com.example.ustock

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.Scaffold

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
                Text(text = "Button 1")
            }
            Button(onClick = { /* Handle Button 2 Click */ }) {
                Text(text = "Button 2")
            }
            // Add more buttons as needed
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePage() {
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
            // Your scrollable content goes here
            Text(text = "No Tasks Yet")

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
