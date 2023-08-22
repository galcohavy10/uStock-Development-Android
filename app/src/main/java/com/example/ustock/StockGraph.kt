package com.example.ustock
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
@Composable
fun LineGraphPreview() {
    MaterialTheme {
        LineGraph(
            data = listOf(100f, 75f, 20f, 50f, 70f, 60f, 80f),
            modifier = Modifier
                .size(200.dp, 100.dp) // Set the specific width and height you desire
        )


    }
}
