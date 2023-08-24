package com.example.ustock



import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LineGraph(data: List<Float>, modifier: Modifier = Modifier) {

    Canvas(modifier = modifier.padding(16.dp)) {
        val path = Path()
        val strokeWidth = Stroke(width = 2f)
        val lineColor = Color.Blue

        if (data.isNotEmpty()) {
            val stepX = size.width / (data.size - 1)
            val maxY = data.maxOrNull() ?: 1f
            val minY = data.minOrNull() ?: 0f
            val valueRange = maxY - minY

            data.forEachIndexed { index, value ->
                val x = index * stepX
                val y = size.height - ((value - minY) / valueRange) * size.height
                val point = Offset(x, y)

                if (index == 0) {
                    path.moveTo(point.x, point.y)
                } else {
                    path.lineTo(point.x, point.y)
                }
            }
        }

        drawPath(path = path, color = lineColor, style = strokeWidth)
    }
}

@Preview(showBackground = true)
//@Composable
//fun LineGraphPreview() {
//    MaterialTheme {
//        LineGraph(
//            data = listOf(100f, 75f, 20f, 50f, 70f, 60f, 80f),
//            modifier = Modifier
//                .size(200.dp, 100.dp) // Set the specific width and height you desire
//        )
//
//
//    }
//}


@Composable
fun StockGraphWithControls() {
    var data by remember { mutableStateOf(listOf(100f, 75f, 20f, 50f, 70f, 60f, 80f)) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("STOCK", fontSize = 18.sp, fontWeight = FontWeight.Bold,textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Y-axis label
            // Y-axis label
            Text("Value",fontSize = 14.sp,textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(50.dp)
                    .rotate(270f) // Rotate the text by 90 degrees
                    .padding(2.dp)
                    .offset(x = 10.dp, y = 0.dp)
            )
            //Line graph
            LineGraph(data = data, modifier = Modifier.fillMaxWidth().height(200.dp).offset(x = -30.dp, y = 0.dp))
        }

        // Buttons to manipulate the x-axis
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { data = data.map { 5f } }) { // Increase all values by 10
                Text("5D")
            }
            Button(onClick = { data = data.map { 7f } }) { // Increase all values by 10
                Text("1W")
            }
            Button(onClick = { data = data.map { 30f } }) { // Increase all values by 10
                Text("1M")
            }
            Button(onClick = { data = data.map { 365f } }) { // Decrease all values by 10
                Text("1Y")
            }
            //TODO:it should be the # of data points that is created
            Button(onClick = { data = data.map { it } }) { // Decrease all values by 10
                Text("ALL")
            }
        }
    }
}